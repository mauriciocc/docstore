package models

import java.util.Date

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._


case class Notification( @Required message: String,
                         @Required userId: Long,
                         creationDate: Date = new Date(),
                         readDate: Option[Date] = None,
                         override val id: Long = 0) extends ActiveRecord {
  lazy val user = belongsTo[User]
}

object Notification extends ActiveRecordCompanion[Notification] with PlayFormSupport[Notification] {

  implicit val format = Json.format[Notification]

  def forUser(userId: Long) = {
    Notification.where(_.userId === userId).toList
  }

  def notifyAllUsers(message: String, users: Seq[Long]) = {
    users.map( userId =>
      Notification(message, userId).create
    )
  }

}
