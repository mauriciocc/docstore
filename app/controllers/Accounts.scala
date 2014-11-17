package controllers

import models._
import play.api.libs.json._
import play.api.mvc._


object Accounts extends Controller with Security {

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(
        Account.forUser(currentUserId)
      ))
  }

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    Account.find(id) match {
      case Some(acc) => Ok(Json.toJson(acc))
      case None => NotFound(s"Account with id '$id not found")
    }
  }

  def save() = HasToken() { _ => currentUserId => implicit req =>
    Account.form.bindFromRequest.fold(
      errors => BadRequest(errors.errorsAsJson),
      account =>
        account.saveEither match {
          case Right(acc) => Ok(Json.toJson(account))
          case Left(errors) => BadRequest(errors.toString())
        }
    )
  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    Account.find(id) match {
      case Some(acc) =>
        acc.delete()
        Ok(s"Account with id '$id removed")
      case None => NotFound
    }
  }

}