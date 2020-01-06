package models

import enumeratum._

sealed abstract class AgeCat(handicap: Double) extends EnumEntry {
  val getHandicapValue: Double = handicap
}


object AgeCat extends Enum[AgeCat] {
  case object M10 extends AgeCat(0.7)
  case object M12 extends AgeCat(0.74)
  case object M14 extends AgeCat(0.8)
  case object M16 extends AgeCat(0.84)
  case object M18 extends AgeCat(0.88)
  case object M20 extends AgeCat(0.93)
  case object M21 extends AgeCat(1.0)
  case object M35 extends AgeCat(0.93)
  case object M40 extends AgeCat(0.89)
  case object M45 extends AgeCat(0.84)
  case object M50 extends AgeCat(0.79)
  case object M55 extends AgeCat(0.74)
  case object M60 extends AgeCat(0.68)
  case object M65 extends AgeCat(0.6)
  case object M70 extends AgeCat(0.53)
  case object M75 extends AgeCat(0.46)
  case object M80 extends AgeCat(0.37)

  case object W10 extends AgeCat(0.6)
  case object W12 extends AgeCat(0.62)
  case object W14 extends AgeCat(0.65)
  case object W16 extends AgeCat(0.67)
  case object W18 extends AgeCat(0.7)
  case object W20 extends AgeCat(0.71)
  case object W21 extends AgeCat(0.8)
  case object W35 extends AgeCat(0.71)
  case object W40 extends AgeCat(0.67)
  case object W45 extends AgeCat(0.62)
  case object W50 extends AgeCat(0.57)
  case object W55 extends AgeCat(0.53)
  case object W60 extends AgeCat(0.48)
  case object W65 extends AgeCat(0.44)
  case object W70 extends AgeCat(0.39)
  case object W75 extends AgeCat(0.35)
  case object W80 extends AgeCat(0.3)

  val values = findValues

  def getAgeCat(str: String): AgeCat = {
    AgeCat.withNameOption(str.toUpperCase) match {
      case Some(ac) => ac
      case None => M21
    }
  }
}
