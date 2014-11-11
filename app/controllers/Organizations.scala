package controllers

import models._
import play.api.libs.json._
import play.api.mvc._


object Organizations extends Controller with Security {

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(Organization.findAllForUser(currentUserId)));
  }

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    Organization.find(id) match {
      case Some(org) => Ok(Json.toJson(org))
      case None => NotFound(s"Organization with id '$id not found")
    }
  }

  def save() = HasToken() { _ => currentUserId => implicit req =>
    Organization.form.bindFromRequest.fold(
      errors => {
        BadRequest(errors.errorsAsJson)
      }, organization =>
        organization.saveEither match {
          case Right(ok) =>
            Ok(Json.toJson(ok))
          case Left(errors) => BadRequest(errors.toString())
        }
    )
  }

  def update(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    Organization.find(id) match {
      case Some(organization) =>
        Organization.form(organization).bindFromRequest.fold(
          errors => {
            BadRequest(errors.errorsAsJson)
          }, organization =>
            organization.saveEither match {
              case Right(ok) =>
                Ok(Json.toJson(ok))
              case Left(errors) => BadRequest(errors.toString())
            }
        )
    }

  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    Organization.find(id) match {
      case Some(org) =>
        org.delete()
        Ok(s"Organization with id '$id removed")
      case None =>
        NotFound
    }

  }

}