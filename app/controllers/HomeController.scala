package controllers

import java.io.File
import java.nio.file.{Files, Path}

import javax.inject._

import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.streams._
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import play.core.parsers.Multipart.FileInfo

import scala.concurrent.{ExecutionContext, Future}
import com.github.tototoshi.csv._
import helpers.CompetitorProcessor
import models.{Competitor, CourseClassType}

case class FormData(name: String)

/**
 * This controller handles a file upload.
 */
@Singleton
class HomeController @Inject() (cc:MessagesControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = Logger(this.getClass)

  val form = Form(
    mapping(
      "name" -> text
    )(FormData.apply)(FormData.unapply)
  )

  /**
   * Renders a start page.
   */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def adultRulesDisplay = Action { implicit request =>
    Ok(views.html.rules.adultRules())
  }

  def openRulesDisplay = Action { implicit request =>
    Ok(views.html.rules.openRules())
  }

  def under16RulesDisplay = Action { implicit request =>
    Ok(views.html.rules.under16Rules())
  }

  /**
   * Uploads a multipart file as a POST request.
   *
   * @return
   */
  def uploadDisplay = Action { implicit request =>
    Ok(views.html.upload_results(form))
  }

  type FilePartHandler[A] = FileInfo => Accumulator[ByteString, FilePart[A]]

  /**
   * Uses a custom FilePartHandler to return a type of "File" rather than
   * using Play's TemporaryFile class.  Deletion must happen explicitly on
   * completion, rather than TemporaryFile (which uses finalization to
   * delete temporary files).
   *
   * @return
   */
  private def handleFilePartAsFile: FilePartHandler[File] = {
    case FileInfo(partName, filename, contentType, _) =>
      val path: Path = Files.createTempFile("multipartBody", "tempFile")
      val fileSink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(path)
      val accumulator: Accumulator[ByteString, IOResult] = Accumulator(fileSink)
      accumulator.map {
        case IOResult(count, status) =>
          logger.debug(s"count = $count, status = $status")
          FilePart(partName, filename, contentType, path.toFile)
      }
  }

  /**
   * Uploads a multipart file as a POST request.
   *
   * @return
   */
  def upload = Action(parse.multipartFormData(handleFilePartAsFile)) { implicit request =>
    val fileOption = request.body.file("results_upload").map {
      case FilePart(key, filename, contentType, file, fileSize, dispositionType) if(filename.toString.contains("csv")) =>
        logger.info(s"key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")

        val reader = CSVReader.open(file)
        val allData = reader.all.drop(1)
        val data = CompetitorProcessor.getCompetitors(allData)
        // logger.info(s"getCompetitors produced: $data")
        reader.close()
        Files.deleteIfExists(file.toPath)
        data

      case FilePart(key, filename, contentType, file, fileSize, dispositionType) =>
        logger.debug(s"Rejected fil: key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")
        s"The file '$filename' does not appear to contain csv data.  Please check and try again."
    }

    fileOption match {
      case Some(comps: Map[_,_]) => Ok(views.html.results_summary(CompetitorProcessor.getCurrentDataSet))

      case Some(x: String) => Ok(s"File load result = $x")

      case _ => Ok(s"File load result = no file")
    }
  }
}
