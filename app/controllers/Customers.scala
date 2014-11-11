package controllers

import models._
import play.api.libs.json._
import play.api.mvc._


object Customers extends Controller with Security {

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(CustomerRepo.findAllForUser(currentUserId)));
  }

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    CustomerRepo.findOne(id) match {
        case Some(org) => Ok(Json.toJson(org))
        case None => NotFound(s"Customer with id '$id not found")
      }
  }

  def save() = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
    req.body.validate[Customer].fold(
      errors => {
        BadRequest(JsError.toFlatJson(errors))
      }, customer => {
        try {
          Ok(Json.toJson(CustomerRepo.save(customer)))
        }
        catch {
          case e: Exception => BadRequest(Json.obj("error" -> e.getMessage))
        }
      }
    )
  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    CustomerRepo.remove(id)
    Ok(s"Customer with id '$id removed")
  }

}