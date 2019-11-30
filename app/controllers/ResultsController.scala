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

/**
 * This controller displaying the results pages.
 */
@Singleton
class ResultsController @Inject() (cc:MessagesControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = Logger(this.getClass)


  def resultsSummaryDisplay = Action { implicit request =>
    Ok(views.html.results_summary(CompetitorProcessor.getCurrentDataSet))
  }

  def resultsDetailedDisplay(course: String, place: Int) = Action { implicit request =>
    val courseClassType = CourseClassType.fromString(course)

    val comp = CompetitorProcessor.getResultsForClass(courseClassType)(place - 1)

    Ok(views.html.results_detailed(courseClassType, comp))
  }

}
