package models

import enumeratum._

sealed abstract class CourseClassType(override val entryName: String) extends EnumEntry

object CourseClassType extends Enum[CourseClassType] {
  case object FullSnooker extends CourseClassType("Full Snooker O")
  case object SimplifiedSnooker extends CourseClassType("Simplified Snooker O")
  case object NormalScore extends CourseClassType("Normal Score")

  val values = findValues

  def fromString(str: String): CourseClassType = {
    println(s"CourseClassType from string called with: $str")
    CourseClassType.withNameInsensitiveOption(str) match {
      case Some(cc) => cc
      case None => NormalScore
    }
  }

}
