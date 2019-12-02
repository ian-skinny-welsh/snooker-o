package controllers

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Path}
import javax.inject._

import helpers.Control._

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
    val baseDir = new File(basePath)
    if(!baseDir.exists()) baseDir.mkdir()

    val summaryResultsPath = basePath + SUMMARY_RESULTS_NAME

    val summaryResultsPage = views.html.results.core.results_wrapper(SUMMARY_RESULTS_TITLE, views.html.results.core.summary_results_core(CompetitorProcessor.getCurrentDataSet, true))

    using(new BufferedWriter(new FileWriter(new File(summaryResultsPath), false))) { outputFile =>
        outputFile.write(summaryResultsPage.toString + "\n")
    }

    val newDir = new File(basePath, DETAILED_RESULTS_PATH)
    if(!newDir.exists()) newDir.mkdir()

    logger.warn(s"Does directory exist: ${newDir.exists()}")

    val data = CompetitorProcessor.getCurrentDataSet
    for(
      courseClassType <- data.keySet;
      (placedCompetitor, place) <- data(courseClassType).zip(LazyList from 1)
    ) yield {

      val detaledResultsPage = views.html.results.core.results_wrapper("Detailed results", views.html.results.core.detailed_results_core(courseClassType, placedCompetitor))
      val path = newDir.getPath + s"\\${courseClassType.toString.toLowerCase}-${place}.html"

      using(new BufferedWriter(new FileWriter(new File(path), false))) { outputFile =>
        outputFile.write(detaledResultsPage.toString + "\n")
      }
    }

    Redirect(routes.ResultsController.summaryResultsDisplay)
  }
}

// TODO: Fix the "Homepage" button in the detailed results EXPORT.  It needs to go back to the exported summary.
// TODO: In the exported detailed results, add a "Next" button that goes to the next competitor
// TODO: Refactor RaceTime - too many classes in one file.