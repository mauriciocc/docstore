package controllers

import models.{EmailAlreadyInUseException, User, UserRepo}
import play.api.i18n.Messages
import play.api.libs.json._
import play.api.mvc._

object Users extends Controller {

  def view(id: Long) = Action { implicit req =>
    Ok(Json.toJson(UserRepo.findOne(id)))
  }

  def save() = Action(BodyParsers.parse.json) { implicit req =>
    req.body.validate[User].fold(
      errors => {
        BadRequest(JsError.toFlatJson(errors))
      }, user => {
        try {
          Ok(Json.toJson(UserRepo.save(user)))
        }
        catch {
          case e: EmailAlreadyInUseException => BadRequest(Json.obj("error" -> Messages("email.already.in.user", e.email)))
          case e: Exception => BadRequest(Json.obj("error" -> e.getMessage))
        }
      }
    )
  }

}