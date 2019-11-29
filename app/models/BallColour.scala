package models

import enumeratum._
import models.BallColour.{IllegalColour, IllegalRed}

sealed abstract class BallColour(override val entryName: String, points: Double, isColour: Boolean) extends EnumEntry {
  val getPointsValue: Double = points

  def isARed = !this.isColour

  def isAColour = this.isColour

  def islegal: Boolean = {
    this match {
      case IllegalColour | IllegalRed => false
      case _ => true
    }
  }

  override def toString: String = this.entryName
}

object BallColour extends Enum[BallColour] {
  case object RedBall extends BallColour("Red", 10, false)

  case object YellowBall extends BallColour("Yellow", 20, true)
  case object GreenBall extends BallColour("Green", 30, true)
  case object BrownBall extends BallColour("Brown", 40, true)
  case object BlueBall extends BallColour("Blue", 50, true)
  case object PinkBall extends BallColour("Pink", 60, true)
  case object BlackBall extends BallColour("Black", 70, true)

  case object UnknownBall extends BallColour("Unknown control", 0, false)

  case object AnyColouredBall extends BallColour("Any colour", 0, true)

  case object IllegalRed extends BallColour("Illegal Red", 0, false)
  case object IllegalColour extends BallColour("Illegal colour", 0, true)

  val values = findValues

  def getNextColour(thisColour: BallColour): BallColour = {
    val thisIndex = BallColour.indexOf(thisColour)
    if(thisIndex < BallColour.indexOf(BlackBall)) {
      values(thisIndex + 1)
    } else {
      IllegalColour
    }
  }
}
