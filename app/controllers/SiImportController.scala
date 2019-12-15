package controllers

import java.nio.file.Files

import javax.inject._
import play.api._
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import com.github.tototoshi.csv._
import helpers.CompetitorProcessor
import models.BallColour.{RedBall, UnknownBall}
import models.FileNameForm

import scala.concurrent.ExecutionContext

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
        val data = CompetitorProcessor.setImportedCsvData(allData)
        // logger.info(s"getCompetitors produced: $data")
        data

      case FilePart(key, filename, contentType, file, fileSize, dispositionType) =>
        logger.debug(s"Rejected file: key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")
        s"The file '$filename' does not appear to contain csv data.  Please check and try again."
    }

    fileOption match {
      case Some(RedBall) => Redirect(routes.ResultsController.summaryResultsDisplay)

      case Some(UnknownBall) => Redirect(routes.ErrorController.basicErrorDisplay("Unable to process the input file, please check it and try again."))

      case Some(x: String) => Redirect(routes.ErrorController.basicErrorDisplay(x))

      case _ => Ok(s"File load result = no file")
    }
  }
}
