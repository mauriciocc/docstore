package controllers

import models.{OrganizationRepo, _}
import play.api.libs.json._
import play.api.mvc._


object Accounts extends Controller with Security {

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(AccountRepo.findAll(currentUserId)));
  }

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    AccountRepo.findOne(id) match {
        case Some(acc) => Ok(Json.toJson(acc))
        case None => NotFound(s"Account with id '$id not found")
      }
  }

  def save() = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
    req.body.validate[Account].fold(
      errors => {
        BadRequest(JsError.toFlatJson(errors))
      }, organization => {
        try {
          Ok(Json.toJson(AccountRepo.save(organization)))
        }
        catch {
          case e: Exception => BadRequest(Json.obj("error" -> e.getMessage))
        }
      }
    )
  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    AccountRepo.remove(id)
    Ok(s"Account with id '$id removed")
  }

}