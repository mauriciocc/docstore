package controllers

import java.nio.file.Files
import java.util.concurrent.TimeUnit

import com.github.aselab.activerecord.{PlayFormSupport, ActiveRecordCompanion}
import models._
import play.api.cache.Cache
import play.api.libs.Files.TemporaryFile
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.duration.Duration


object Documents extends Crud[Document] {

  override val companion: ActiveRecordCompanion[Document] with PlayFormSupport[Document] = Document
  override implicit val format: Format[Document] = Document.format

  def findAll(customerId: Long) = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(Document.forCustomer(customerId)));
  }

  /*def save() = HasToken(BodyParsers.parse.json) { _ => currentUserId => implicit req =>
    req.body.validate[Document].fold(
      errors => {
        BadRequest(JsError.toFlatJson(errors))
      }, document => {
        try {
          val key = currentUserId + "upload"
          val doc = Cache.get(key) match {
            case Some(value) =>
              val file = value.asInstanceOf[play.api.mvc.MultipartFormData.FilePart[TemporaryFile]]
              val bytes = Files.readAllBytes(file.ref.file.toPath)
              val contentType = file.contentType.getOrElse("application/octet-stream")
              val dbFile = DatabaseFileRepo.save(DatabaseFile(None, file.filename, contentType, bytes))
              /*DocumentRepo.save(document.copy(databaseFileId = dbFile.id));*/
            case None =>
              NotFound
              /*document.id.getOrElse(throw new IllegalStateException())
              DocumentRepo.save(document)*/
          }
          Cache.remove(key)
          Ok(Json.toJson(doc))
        }
        catch {
          case e: Exception => BadRequest(Json.obj("error" -> e.getMessage))
        }
      }
    )
  }*/

  def upload = HasToken(parse.multipartFormData) { _ => userId => request =>
    request.body.file("file").map { file =>
      Cache.set(userId + "upload", file, Duration(5, TimeUnit.MINUTES))
      Ok("File uploaded")
    }.getOrElse {
      BadRequest("Error on upload")
    }
  }

  def download(id: Long) = Action { implicit request =>
    val dbFile = DocumentRepo.databaseFileFor(id).get
    var disposition = s"""inline; filename="${dbFile.name}"""""
    Ok(dbFile.content).withHeaders(
      CONTENT_TYPE -> dbFile.contentType,
      CONTENT_DISPOSITION -> disposition
    )
  }


}