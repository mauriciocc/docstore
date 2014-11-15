package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._

case class Account(@Required name: String,
                   userId: Long,
                   override val id: Long = 0) extends ActiveRecord {
  lazy val user = belongsTo[User]
  lazy val organizations = hasMany[Organization]
}

object Account extends ActiveRecordCompanion[Account] with PlayFormSupport[Account] {
  implicit val format = Json.format[Account]

  def forUser(id: Long): List[Account] = {
    User.find(id) match {
      case Some(user) =>
        user.accounts.toList
    }
  }

}