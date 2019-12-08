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
import models.FileNameForm

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller handles a file upload.
 */
@Singleton
class SiImportController @Inject()(cc:MessagesControllerComponents)
                                  (implicit executionContext: ExecutionContext)
  extends AbstractFileController(cc) {

  private val logger = Logger(this.getClass)

  /**
   * Uploads a multipart file as a POST request.
   *
   * @return
   */
  def importResultsDisplay = Action { implicit request =>
    Ok(views.html.import_results(FileNameForm.form))
  }

  /**
   * Uploads a multipart file as a POST request.
   *
   * @return
   */
  def importResultsSubmit = Action(parse.multipartFormData(handleFilePartAsFile)) { implicit request =>
    val fileOption = request.body.file("import_results").map {
      case FilePart(key, filename, contentType, file, fileSize, dispositionType) if(filename.toString.contains("csv")) =>
        logger.debug(s"key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")

        val reader = CSVReader.open(file)
        val allData = reader.all.drop(1)
        reader.close()
        Files.deleteIfExists(file.toPath)
        val data = CompetitorProcessor.getCompetitors(allData)
        // logger.info(s"getCompetitors produced: $data")
        data

      case FilePart(key, filename, contentType, file, fileSize, dispositionType) =>
        logger.debug(s"Rejected file: key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")
        s"The file '$filename' does not appear to contain csv data.  Please check and try again."
    }

    fileOption match {
      case Some(comps: Map[_,_]) => Redirect(routes.ResultsController.summaryResultsDisplay)

      case Some(x: String) => Ok(s"File load result = $x")

      case _ => Ok(s"File load result = no file")
    }
  }
}
