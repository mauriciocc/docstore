package models

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json._


case class Notification( @Required message: String,
                         @Required userId: Long,
                         createdAt: Timestamp = new Timestamp(new Date().getTime),
                         readAt: Option[Timestamp] = None,
                         override val id: Long = 0) extends ActiveRecord {
  lazy val user = belongsTo[User]
}

object Notification extends ActiveRecordCompanion[Notification] with PlayFormSupport[Notification] {

  implicit val locationFormat: play.api.libs.json.Format[Timestamp] = new play.api.libs.json.Format[Timestamp] {
    override def reads(json: JsValue): JsResult[Timestamp] = {
      JsSuccess(new Timestamp(json.as[Long]))
    }
    override def writes(o: Timestamp): JsValue = {
      JsNumber(o.getTime)
    }
  }

  implicit val format = Json.format[Notification]

  def forUser(userId: Long) = {
    Notification.where(_.userId === userId).orderBy(_.createdAt desc, _.readAt).limit(10).toList
  }

  def notifyAllUsers(message: String, users: Seq[Long]) = {
    users.map( userId =>
      Notification(message, userId).create
    )
  }

}
