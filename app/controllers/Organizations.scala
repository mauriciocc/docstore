package controllers

import models.{OrganizationRepo, _}
import play.api.libs.json._
import play.api.mvc._


object Organizations extends Controller with Security {

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(OrganizationRepo.findAll(currentUserId)));
  }

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    OrganizationRepo.findOne(id) match {
        case Some(org) => Ok(Json.toJson(org))
        case None => NotFound(s"Organization with id '$id not found")
      }
  }

  def save() = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
    req.body.validate[Organization].fold(
      errors => {
        BadRequest(JsError.toFlatJson(errors))
      }, organization => {
        try {
          Ok(Json.toJson(OrganizationRepo.save(organization)))
        }
        catch {
          case e: Exception => BadRequest(Json.obj("error" -> e.getMessage))
        }
      }
    )
  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    OrganizationRepo.remove(id)
    Ok(s"Organization with id '$id removed")
  }

}