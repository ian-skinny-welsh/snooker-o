package models

import enumeratum._
import models.BallColour.{IllegalColour, IllegalRed}
import play.twirl.api.Html

sealed abstract class BallColour(override val entryName: String, points: Double, isColour: Boolean, colourVal: String, textVal: String) extends EnumEntry {
  val getPointsValue: Double = points

  val getPenaltyValue: Double = - Math.max(40, points)

  def isARed = !this.isColour

  def isAColour = this.isColour

  def islegal: Boolean = {
    this match {
      case IllegalColour | IllegalRed => false
      case _ => true
    }
  }

  def getBallImage: Html = {
    Html(s"<span class='badge badge-pill' style='background-color:${colourVal}; color:${colourVal}'>${textVal}</span>")
  }

  override def toString: String = this.entryName
}

object BallColour extends Enum[BallColour] {
  case object RedBall extends BallColour("Red", 10, false, "#c02026", ".")

  case object YellowBall extends BallColour("Yellow", 20, true, "#fbab19", ".")
  case object GreenBall extends BallColour("Green", 30, true, "#00aa70", ".")
  case object BrownBall extends BallColour("Brown", 40, true, "#ba5427", ".")
  case object BlueBall extends BallColour("Blue", 50, true, "#0071a9", ".")
  case object PinkBall extends BallColour("Pink", 60, true, "#f26d70", ".")
  case object BlackBall extends BallColour("Black", 70, true, "#0f0e0d", ".")

  case object UnknownBall extends BallColour("Unknown control", 0, false, "#c3c3c3", "?")

  case object AnyColouredBall extends BallColour("Any colour", 0, true, "#b83dba", ".")

  case object IllegalRed extends BallColour("Illegal Red", 0, false, "#c02026", "X"){
    override def getBallImage: Html = {
      Html(s"<span class='badge badge-pill' style='background-color:#c02026;'>X</span>")
    }
  }
  case object IllegalColour extends BallColour("Illegal colour", 0, true, "#b83dba", "X"){
    override def getBallImage: Html = {
      Html(s"<span class='badge badge-pill' style='background-color:#b83dba;'>X</span>")
    }
  }

  val values = findValues

  def getBallFromName(name: String): BallColour = {
    BallColour.withNameOption(name) match {
      case Some(bc) => bc
      case None => UnknownBall
    }
  }

  def getNextColour(thisColour: BallColour): BallColour = {
    val thisIndex = BallColour.indexOf(thisColour)
    if(thisIndex < BallColour.indexOf(BlackBall)) {
      values(thisIndex + 1)
    } else {
      IllegalColour
    }
  }
}
