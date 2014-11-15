package controllers

import com.github.aselab.activerecord.{ActiveRecord, ActiveRecordCompanion, PlayFormSupport}
import play.api.libs.json.{Format, Json}
import play.api.mvc.Controller

trait Crud[T <: ActiveRecord] extends Controller with Security {

  val companion: ActiveRecordCompanion[T] with PlayFormSupport[T]
  implicit val format: Format[T]

  def findOne(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    companion.find(id) match {
      case Some(entity) =>
        Ok(Json.toJson(entity))
      case None =>
        NotFound
    }
  }

  def save() = HasToken() { _ => currentUserId => implicit req =>
    companion.form.bindFromRequest.fold(
      errors => {
        BadRequest(errors.errorsAsJson)
      }, entity =>
        entity.saveEither match {
          case Right(ok) =>
            Ok(Json.toJson(ok))
          case Left(errors) => BadRequest(errors.toString())
        }
    )
  }

  def update(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    companion.find(id) match {
      case Some(entity) =>
        companion.form(entity).bindFromRequest.fold(
          errors => {
            BadRequest(errors.errorsAsJson)
          }, entity =>
            entity.saveEither match {
              case Right(ok) =>
                Ok(Json.toJson(ok))
              case Left(errors) => BadRequest(errors.toString())
            }
        )
    }
  }

  def remove(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    companion.find(id) match {
      case Some(entity) =>
        entity.delete()
        Ok
      case None => NotFound
    }
  }

}