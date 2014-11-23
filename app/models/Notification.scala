package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormat, DateTimeFormatter}
import play.api.libs.json._
import utils.JsonFormats


case class Notification( @Required message: String,
                         @Required userId: Long,
                         createdAt: Timestamp = new Timestamp(new Date().getTime),
                         readAt: Option[Timestamp] = None,
                         override val id: Long = 0) extends ActiveRecord {
  lazy val user = belongsTo[User]
}

object Notification extends ActiveRecordCompanion[Notification] with PlayFormSupport[Notification] {

  implicit val timestampFormat = JsonFormats.timestampFormat
  implicit val format = Json.format[Notification]

  def forUser(userId: Long) = {
    Notification.where(_.userId === userId).orderBy(_.createdAt desc, _.readAt desc).limit(10).toList
  }

  def notifyAllUsers(message: String, users: Seq[Long]) = {
    users.map( userId =>
      Notification(message, userId).create
    )
  }

}
