package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent.ExecutionContext
import helpers.{CompetitorProcessor, ExportHelper}
import models.{CourseClassType, FileNameForm}

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

    val userMsg = if(ExportHelper.exportResults()) {
      "exportMsg" -> "The results have been exported."
    } else {
      "exportError" -> "The export failed, the application log file will contain technical information."
    }

    Redirect(routes.ResultsController.exportResultsDisplay).flashing(userMsg)
  }

}
