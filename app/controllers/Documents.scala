package controllers

import java.nio.file.Files
import java.util.concurrent.TimeUnit

import com.github.aselab.activerecord.{ActiveRecordCompanion, PlayFormSupport}
import models._
import play.api.cache.Cache
import play.api.libs.Files.TemporaryFile
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.duration.Duration


object Documents extends Crud[Document] {

  override val companion: ActiveRecordCompanion[Document] with PlayFormSupport[Document] = Document
  override implicit val format = Document.format

  def findAll(customerId: Long) = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(Document.forCustomer(customerId)));
  }


  /*override def save() = HasToken() { _ => currentUserId => implicit req =>
    super.save()
/*    */

  }*/

  override def save() = HasToken() { _ => currentUserId => implicit req =>
    companion.form.bindFromRequest.fold(
      errors => {
        BadRequest(errors.errorsAsJson)
      }, entity =>
        entity.saveEither match {
          case Right(ok) =>
            val key = currentUserId + "upload"
            val doc = Cache.get(key) match {
              case Some(value) =>
                val file = value.asInstanceOf[play.api.mvc.MultipartFormData.FilePart[TemporaryFile]]
                val bytes = Files.readAllBytes(file.ref.file.toPath)
                val contentType = file.contentType.getOrElse("application/octet-stream")
                val dbFile = DatabaseFile(file.filename, contentType, bytes).create
                val okUpdated = ok.copy(databaseFileId = Some(dbFile.id)).update
            }
            Ok(Json.toJson(ok))
          case Left(errors) => BadRequest(errors.toString())
        }
    )

  }

  def upload = HasToken(parse.multipartFormData) { _ => userId => request =>
    request.body.file("file").map { file =>
      Cache.set(userId + "upload", file, Duration(5, TimeUnit.MINUTES))
      Ok("File uploaded")
    }.getOrElse {
      BadRequest("Error on upload")
    }
  }



  def download(id: Long) = Action { implicit request =>
    val dbFile = Document.databaseFileFor(id).get
    var disposition = s"""inline; filename="${dbFile.name}"""""
    Ok(dbFile.content).withHeaders(
      CONTENT_TYPE -> dbFile.contentType,
      CONTENT_DISPOSITION -> disposition
    )
  }


}