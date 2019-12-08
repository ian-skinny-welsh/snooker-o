package controllers

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Path}
import javax.inject._

import helpers.ResourceManager._

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
import play.twirl.api.Html
import com.typesafe.config._

import scala.concurrent.{ExecutionContext, Future}

import helpers.CompetitorProcessor
import models.{FileNameForm, CourseClassType}
import models.DataFileConstants._

/**
 * This controller displays the results pages.
 */
@Singleton
class ResultsController @Inject() (cc:MessagesControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = Logger(this.getClass)


  def summaryResultsDisplay = Action { implicit request =>
    Ok(views.html.results.summary_results(CompetitorProcessor.getCurrentDataSet))
  }

  def detailedResultsDisplay(course: String, place: Int) = Action { implicit request =>
    val courseClassType = CourseClassType.fromString(course)

    val comp = CompetitorProcessor.getResultsForClass(courseClassType)(place - 1)

    Ok(views.html.results.detailed_results(courseClassType, comp))
  }

  def exportResultsDisplay = Action { implicit request =>
    Ok(views.html.results.export_results(FileNameForm.form))
  }


  def exportResultsSubmit = Action { implicit request =>

    val conf = ConfigFactory.load
    val basePath = conf.getString("export.basePath")
    createDirectories(basePath)

    val summaryResultsPath = basePath + SUMMARY_RESULTS_NAME
    val summaryResultsPage = views.html.results.core.results_wrapper(SUMMARY_RESULTS_TITLE,
      views.html.results.core.summary_results_core(CompetitorProcessor.getCurrentDataSet, true))
    outputToFile(summaryResultsPath, summaryResultsPage.toString)

    val data = CompetitorProcessor.getCurrentDataSet
    for(
      courseClassType <- data.keySet;
      numEntries = data(courseClassType).size;
      placedCompetitor  <- data(courseClassType)
    ) yield {
      val detailedResultsPage = views.html.results.core.results_wrapper("Detailed results",
        views.html.results.core.detailed_results_core(courseClassType, placedCompetitor, true, numEntries))
      val path = basePath + s"${DETAILED_RESULTS_PATH}\\${courseClassType.toString.toLowerCase}-${placedCompetitor.place}.html"
      outputToFile(path, detailedResultsPage.toString)
    }

    Redirect(routes.ResultsController.summaryResultsDisplay)
  }

  private def outputToFile(path: String, content: String) = {
    using(new BufferedWriter(new FileWriter(new File(path), false))) { outputFile =>
      outputFile.write(content)
    }
  }

  private def createDirectories(basePath: String) = {
    val baseDir = new File(basePath)
    if(!baseDir.exists()) baseDir.mkdir()

    val subDir = new File(basePath, DETAILED_RESULTS_PATH)
    if(!subDir.exists()) subDir.mkdir()

    logger.warn(s"Does directory exist: ${subDir.exists()}")
  }
}

// TODO: Refactor RaceTime - too many classes in one file.