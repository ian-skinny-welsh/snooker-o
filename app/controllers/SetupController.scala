package controllers

import java.nio.file.Files

import javax.inject._

import play.api._
import play.api.data.Form
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._

import scala.concurrent.ExecutionContext
import com.github.tototoshi.csv._

import helpers.BallControlsInUse
import models.BallColour.{BlackBall, BlueBall, BrownBall, GreenBall, PinkBall, RedBall, YellowBall}
import models.{BallColour, ControlsForBalls}
import models.FileNameForm

@Singleton
class SetupController @Inject() (cc:MessagesControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends AbstractFileController(cc) {

  private val logger = Logger(this.getClass)

  private def  utilSetupControlsDisplay(formData: Form[ControlsForBalls], msg: String)(implicit request: MessagesRequestHeader) =  {
    Ok(views.html.setup_controls(formData, msg))
  }

  def setupControlsDisplay(msg: String) = Action { implicit request =>

    val ctrlsForBalls = ControlsForBalls(getCtrlNumbers(RedBall),
      getCtrlNumbers(YellowBall),
      getCtrlNumbers(GreenBall),
      getCtrlNumbers(BrownBall),
      getCtrlNumbers(BlueBall),
      getCtrlNumbers(PinkBall),
      getCtrlNumbers(BlackBall)
    )

    val formData = ControlsForBalls.ctrlsForBallsForm.fill(ctrlsForBalls)

    utilSetupControlsDisplay(formData, msg)
  }

  def setupControlsSubmit = Action { implicit request =>
    ControlsForBalls.ctrlsForBallsForm.bindFromRequest.fold(
      formWithErrors => {
        utilSetupControlsDisplay(formWithErrors, "Only enter a comma separated list of control numbers for each 'ball'")
      },
      newBallCtrlData => {
        val temp = newBallCtrlData.getAsMap
        BallControlsInUse.setBallControlsInUse(temp)

        Redirect(routes.SiImportController.importResultsDisplay)
      }
    )
  }

  def importControlsDisplay = Action { implicit request =>
    Ok(views.html.import_controls(FileNameForm.form))
  }

  private def getCtrlNumbers(ballColour: BallColour): String = {
    BallControlsInUse.getControlsForBall(ballColour).map(_.ctrlCode).mkString(", ")
  }

  def importControlsSubmit = Action(parse.multipartFormData(handleFilePartAsFile)) { implicit request =>
    val fileOption = request.body.file("import_controls").map {
      case FilePart(key, filename, contentType, file, fileSize, dispositionType) if(filename.toString.contains("csv")) =>
        logger.debug(s"key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")

        val reader = CSVReader.open(file)
        val allData = reader.all
        reader.close()
        Files.deleteIfExists(file.toPath)
        val data = try {
          BallControlsInUse.getControlsFromFileData(allData)
        } catch {
          case nfe: NumberFormatException => s"Could not process the file, looks like a bad control number: ${nfe.getMessage}"
          case ue: Exception => s"Could not process the file, reason unknown: ${ue.getMessage}"
        }
        // logger.info(s"getControlsFromFileData produced: $data")
        data

      case FilePart(key, filename, contentType, file, fileSize, dispositionType) =>
        logger.debug(s"Rejected file: key = $key, filename = $filename, contentType = $contentType, file = $file, fileSize = $fileSize, dispositionType = $dispositionType")
        s"The file '$filename' does not appear to contain csv data.  Please check and try again."
    }

    fileOption match {
      case Some(controls: Map[_,_]) => Redirect(routes.SetupController.setupControlsDisplay("Please confirm your controls are correct."))

      case Some(x: String) => Redirect(routes.ErrorController.basicErrorDisplay(x))

      case _ => Ok(s"File load result = no file")
    }
  }
}
