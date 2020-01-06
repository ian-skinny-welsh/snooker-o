package models

import play.api.data.Form
import play.api.data.Forms._

case class CourseLists(courseNames: List[String], courseClassTypes: List[String])

object CourseLists {
  val courseListsForm = Form(
    mapping(
      "courseNames" -> list(text),
      "courseMappings" -> list(text)
    )(CourseLists.apply)(CourseLists.unapply)
  )
}