package controllers

import models.{OrganizationRepo, _}
import play.api.libs.json._
import play.api.mvc._


object Offices extends Controller with Security {

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(OfficeRepo.findAllForUser(currentUserId)));
  }

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    OfficeRepo.findOne(id) match {
        case Some(org) => Ok(Json.toJson(org))
        case None => NotFound(s"Office with id '$id not found")
      }
  }

  def save() = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
    req.body.validate[Office].fold(
      errors => {
        BadRequest(JsError.toFlatJson(errors))
      }, office => {
        try {
          Ok(Json.toJson(OfficeRepo.save(office)))
        }
        catch {
          case e: Exception => BadRequest(Json.obj("error" -> e.getMessage))
        }
      }
    )
  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    OfficeRepo.remove(id)
    Ok(s"Office with id '$id removed")
  }

}