package models

import enumeratum._

sealed trait AgeCat extends EnumEntry


object AgeCat extends Enum[AgeCat] {
  case object M10 extends AgeCat
  case object M12 extends AgeCat
  case object M14 extends AgeCat
  case object M16 extends AgeCat
  case object M18 extends AgeCat
  case object M20 extends AgeCat
  case object M21 extends AgeCat
  case object M35 extends AgeCat
  case object M40 extends AgeCat
  case object M45 extends AgeCat
  case object M50 extends AgeCat
  case object M55 extends AgeCat
  case object M60 extends AgeCat
  case object M65 extends AgeCat
  case object M70 extends AgeCat
  case object M75 extends AgeCat
  case object M80 extends AgeCat

  case object W10 extends AgeCat
  case object W12 extends AgeCat
  case object W14 extends AgeCat
  case object W16 extends AgeCat
  case object W18 extends AgeCat
  case object W20 extends AgeCat
  case object W21 extends AgeCat
  case object W35 extends AgeCat
  case object W40 extends AgeCat
  case object W45 extends AgeCat
  case object W50 extends AgeCat
  case object W55 extends AgeCat
  case object W60 extends AgeCat
  case object W65 extends AgeCat
  case object W70 extends AgeCat
  case object W75 extends AgeCat
  case object W80 extends AgeCat

  val values = findValues

  def getAgeCat(str: String): AgeCat = {
    AgeCat.withNameOption(str) match {
      case Some(ac) => ac
      case None => M21
    }
  }
}
