package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.BallColour.{BlackBall, BlueBall, BrownBall, GreenBall, PinkBall, RedBall, YellowBall}

case class ControlsForBalls(red: String,
                            yellow: String,
                            green: String,
                            brown: String,
                            blue: String,
                            pink: String,
                            black: String){

  def getAsMap:Map[BallColour, List[ControlCode]] ={
    Map(RedBall -> convertStringToControls(red),
      YellowBall -> convertStringToControls(yellow),
      GreenBall -> convertStringToControls(green),
      BrownBall -> convertStringToControls(brown),
      BlueBall -> convertStringToControls(blue),
      PinkBall -> convertStringToControls(pink),
      BlackBall -> convertStringToControls(black)
    )
  }

  def convertStringToControls(strData: String): List[ControlCode] = {
    strData.replace(" ", "").split(",").map(ct => ControlCode(ct.toInt)).toList
  }
}

object ControlsForBalls {
  val patt = pattern("""[0-9, ]+""".r, error = "Only numbers, commas and spaces please")

  val ctrlsForBallsForm = Form(
    mapping(
      "Red" -> text.verifying(patt),
      "Yellow" -> text.verifying(patt),
      "Green" -> text.verifying(patt),
      "Brown" -> text.verifying(patt),
      "Blue" -> text.verifying(patt),
      "Pink" -> text.verifying(patt),
      "Black" -> text.verifying(patt)
    )(ControlsForBalls.apply)(ControlsForBalls.unapply)
  )
}
