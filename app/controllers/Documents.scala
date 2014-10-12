package controllers

import java.nio.file.Files

import models.{OrganizationRepo, _}
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.duration.Duration


object Documents extends Controller with Security {

  def findAll(customerId: Long) = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(DocumentRepo.findAllForCustomer(customerId)));
  }

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    DocumentRepo.findOne(id) match {
        case Some(org) => Ok(Json.toJson(org))
        case None => NotFound(s"Customer with id '$id not found")
      }
  }

  def save() = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
    val file = Cache.get(currentUserId+"upload")
    if(file.isEmpty) {
      BadRequest("No file")
    }

    req.body.validate[Document].fold(
      errors => {
        BadRequest(JsError.toFlatJson(errors))
      }, document => {
        try {
          Ok(Json.toJson(DocumentRepo.save(document)))
        }
        catch {
          case e: Exception => BadRequest(Json.obj("error" -> e.getMessage))
        }
      }
    )
  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    DocumentRepo.remove(id)
    Ok(s"Document with id '$id removed")
  }

  def upload = HasToken(parse.temporaryFile) { _ => userId => request =>
    Cache.set(userId+"upload", request.body.file)
    Ok("File uploaded")
  }

}