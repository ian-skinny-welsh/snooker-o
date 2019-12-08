package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class ErrorController @Inject()(cc:MessagesControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = Logger(this.getClass)

  def basicErrorDisplay(msg: String) = Action { implicit request =>
    Ok(views.html.errorPage(msg))
  }
}
