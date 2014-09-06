package controllers

import models.User
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Users extends Controller {

  def view(id: Long) = Action { implicit req =>
      Ok(Json.toJson(User(None, "mauriciocc@outlook.com", "mauriciocc", "Mauricio Colognese Concatto", "28091990")))
  }

  def save() = Action(BodyParsers.parse.json) { implicit req =>
    req.body.validate[User].fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
      }, user => {
        Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+user.name+"' saved.") ))
      }
    )
  }

}