package controllers

import models.{Account, User}
import play.api.libs.json._
import play.api.mvc._

object Users extends Controller {

  def view(id: Long) = Action { implicit req =>
    Ok(Json.toJson(User.find(id)))
  }

  def save() = Action { implicit req =>
    User.form.bindFromRequest.fold(
      errors => {
        BadRequest(errors.errorsAsJson)
      }, user =>
        user.saveEither match {
          case Right(u) =>
            Ok(Json.toJson(u))
          case Left(errors) => BadRequest(errors.toString())
        }
    )
  }

  def createAccount() = Action { implicit req =>
    User.form.bindFromRequest.fold(
      errors => {
        BadRequest(errors.errorsAsJson)
      }, user =>
        user.saveEither match {
          case Right(u) =>
            Ok(
              Json.toJson(
                Json.obj(
                  "user" -> u,
                  "account" -> Account(name = u.name, userId = u.id).create
                )
              )
            )
          case Left(errors) => BadRequest(errors.toString())
        }
    )
  }

}