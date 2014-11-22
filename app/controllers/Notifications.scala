package controllers

import java.nio.file.Files
import java.util.Date
import java.util.concurrent.TimeUnit

import com.github.aselab.activerecord.{ActiveRecordCompanion, PlayFormSupport}
import models._
import play.api.cache.Cache
import play.api.libs.Files.TemporaryFile
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.duration.Duration


object Notifications extends Crud[Notification] {

  override val companion: ActiveRecordCompanion[Notification] with PlayFormSupport[Notification] = Notification
  override implicit val format = Notification.format

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(Notification.forUser(currentUserId)))
  }

  def markAsRead(id: Long) = HasToken() { _ => currentUserId => implicit request =>
    Notification.find(id) match {
      case Some(notification) =>
        Ok(Json.toJson(
          notification.copy(readDate = Some(new Date())).update
        ))
      case None =>
        NotFound
    }

  }

}