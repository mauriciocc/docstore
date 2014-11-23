package controllers

import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import models._
import play.api.cache.Cache
import play.api.libs.Files.TemporaryFile
import play.api.libs.json._

import scala.concurrent.duration.Duration


object Documents extends Crud[Document] {

  override val companion: ActiveRecordCompanion[Document] with PlayFormSupport[Document] = Document
  override implicit val format = Document.format

  implicit val findAllWrites = new Writes[List[(Document, Customer)]] {
    override def writes(o: List[(Document, Customer)]): JsValue = {
      JsArray(
        o.map({ row =>
          Json.obj(
            "document" -> Json.toJson(row._1),
            "customer" -> Json.toJson(row._2)
          )
        })
      )
    }
  }

  def findAll(customerId: Long) = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(
      Document.forCustomer(customerId)
    ))
  }

  override def save() = HasToken() { _ => currentUserId => implicit req =>
    companion.form.bindFromRequest.fold(
      errors => {
        BadRequest(errors.errorsAsJson)
      }, entity => {
        val isNewRecord = entity.isNewRecord
        entity.saveEither match {
          case Right(ok) =>
            val key = currentUserId + "upload"
            val doc = Cache.get(key) match {
              case Some(value) =>
                val file = value.asInstanceOf[play.api.mvc.MultipartFormData.FilePart[TemporaryFile]]
                val bytes = Files.readAllBytes(file.ref.file.toPath)
                val contentType = file.contentType.getOrElse("application/octet-stream")
                val dbFile = DatabaseFile(file.filename, contentType, bytes, bytes.length).create
                val okUpdated = ok.copy(databaseFileId = Some(dbFile.id)).update
            }

            Notification.notifyAllUsers(
              s"Um novo documento foi adicionado para o cliente <b>${ok.customer.toOption.get.name}</b>. O nome do documento é <b><i>${ok.name}</i></b> " + (if (ok.dueDate.isDefined) " e vencerá no dia " + new SimpleDateFormat("dd/MM/yyyy").format(ok.dueDate.get) else "" + "."),
              Customer.responsiblesUsers(ok.customerId)
            )
            Ok(Json.toJson(ok))
          case Left(errors) => BadRequest(errors.toString())
        }
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


  def download(id: Long) = HasToken() { _ => currentUserId => implicit req =>
    Document.databaseFileFor(id) match {
      case Some(dbFile) =>
        var disposition = s"""inline; filename="${dbFile.name}"""""
        DatabaseFileDownload(currentUserId, dbFile.id).create
        Ok(dbFile.content).withHeaders(
          CONTENT_TYPE -> dbFile.contentType,
          CONTENT_DISPOSITION -> disposition
        )
      case None =>
        NotFound
    }

  }


}