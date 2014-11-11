package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json
import play.api.libs.json.Json


case class Organization(name: String, accountId: Long, override val id: Long) extends ActiveRecord {
  lazy val account = belongsTo[Account]
}

object Organization extends ActiveRecordCompanion[Organization] with PlayFormSupport[Organization] {

  implicit val format: json.Format[Organization] = Json.format[Organization]

  def findAllForUser(userId: Long) = {
    val accounts = Account.select(_.id).where(_.ownerId === userId).toList
    Organization.where(_.accountId in accounts).toList
  }

}