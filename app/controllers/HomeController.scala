package controllers

import javax.inject._

import play.api._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
@Singleton
class HomeController @Inject() (cc:MessagesControllerComponents)
                               (implicit executionContext: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = Logger(this.getClass)

  /**
   * Renders a start page.
   */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def adultRulesDisplay = Action { implicit request =>
    Ok(views.html.rules.adultRules())
  }

  def openRulesDisplay = Action { implicit request =>
    Ok(views.html.rules.openRules())
  }

  def under16RulesDisplay = Action { implicit request =>
    Ok(views.html.rules.under16Rules())
  }
}
