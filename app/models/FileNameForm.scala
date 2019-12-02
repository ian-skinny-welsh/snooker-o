package models

import play.api.data.Form
import play.api.data.Forms._

case class FileNameForm(name: String)

object FileNameForm{

  val form = Form(
    mapping(
      "name" -> text
    )(FileNameForm.apply)(FileNameForm.unapply)
  )

}
