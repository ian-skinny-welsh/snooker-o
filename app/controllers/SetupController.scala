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
import helpers.{BallControlsInUse, CompetitorProcessor}
import models.BallColour.{BlackBall, BlueBall, BrownBall, GreenBall, PinkBall, RedBall, YellowBall}
import models.{BallColour, Competitor, ControlsForBalls, CourseClassType}


@Singleton
class SetupController @Inject() (cc:MessagesControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = Logger(this.getClass)


  def setupControlsDisplay = Action { implicit request =>

    val ctrlsForBalls = ControlsForBalls(getCtrlNumbers(RedBall),
      getCtrlNumbers(YellowBall),
      getCtrlNumbers(GreenBall),
      getCtrlNumbers(BrownBall),
      getCtrlNumbers(BlueBall),
      getCtrlNumbers(PinkBall),
      getCtrlNumbers(BlackBall)
    )

    val formData = ControlsForBalls.ctrlsForBallsForm.fill(ctrlsForBalls)

    Ok(views.html.setup_controls(formData))
  }

  def setupControlsSubmit = Action { implicit request =>
    ControlsForBalls.ctrlsForBallsForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.setup_controls(formWithErrors))
      },
      newBallCtrlData => {
        val temp = newBallCtrlData.getAsMap
        BallControlsInUse.setBallControlsInUse(temp)

        Redirect(routes.HomeController.uploadDisplay)
      }
    )
  }

  private def getCtrlNumbers(ballColour: BallColour): String = {
    BallControlsInUse.getControlsForBall(ballColour).map(_.ctrlCode).mkString(", ")
  }
}
