package models

import enumeratum._
import models.BallColour.{IllegalColour, IllegalRed, RedBall}

sealed abstract class BallColour(points: Double, isColour: Boolean) extends EnumEntry {
  val getPointsValue: Double = points

  def isARed = !this.isColour

  def isAColour = this.isColour

  def islegal: Boolean = {
    this match {
      case IllegalColour | IllegalRed => false
      case _ => true
    }
  }
}

object BallColour extends Enum[BallColour] {
  case object RedBall extends BallColour(10, false)

  case object YellowBall extends BallColour(20, true)
  case object GreenBall extends BallColour(30, true)
  case object BrownBall extends BallColour(40, true)
  case object BlueBall extends BallColour(50, true)
  case object PinkBall extends BallColour(60, true)
  case object BlackBall extends BallColour(70, true)
  case object AnyColouredBall extends BallColour(0, true)

  case object IllegalRed extends BallColour(0, false)
  case object IllegalColour extends BallColour(0, true)

  case object UnknownBall extends BallColour(0, false)

  val values = findValues

}
