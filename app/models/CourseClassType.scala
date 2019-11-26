package models

import enumeratum._

sealed trait CourseClassType extends EnumEntry

object CourseClassType extends Enum[CourseClassType] {
  case object Adults extends CourseClassType
  case object OpenOrder extends CourseClassType
  case object Under16 extends CourseClassType

  val values = findValues

  def getCourseClass(str: String): CourseClassType = {
    CourseClassType.withNameInsensitiveOption(str.replaceAll(" ", "")) match {
      case Some(cc) => cc
      case None => OpenOrder
    }
  }

}
