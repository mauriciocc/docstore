package controllers

import com.github.aselab.activerecord.{ActiveRecordCompanion, PlayFormSupport}
import models._
import play.api.libs.json._


object Offices extends Crud[Office] {

  override val companion: ActiveRecordCompanion[Office] with PlayFormSupport[Office] = Office
  override implicit val format: Format[Office] = Office.format

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(Office.forUser(currentUserId))); //TODO
  }

  /*
  val crud = new BaseCrud[Office](Office)

    def findAll() = HasToken() { _ => currentUserId => implicit request =>
      Ok(Json.toJson(Office.find(currentUserId))); //TODO
    }

    def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
      crud.findOne(id)
    }

    def save() = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
      crud.create();
    }

    def update(id: Long) = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
      crud.update(id);
    }

    def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
      crud.delete(id)
    }*/

}