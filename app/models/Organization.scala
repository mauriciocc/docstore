package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._


case class Organization(name: String, accountId: Long, override val id: Long) extends ActiveRecord {
  lazy val account = belongsTo[Account]
  lazy val offices = hasMany[Office]
}

object Organization extends ActiveRecordCompanion[Organization] with PlayFormSupport[Organization] {

  implicit val format = Json.format[Organization]

  def forUser(id: Long) = {
    Account.forUser(id).flatten(acc =>
      acc.organizations.orderBy(_.name).toList
    )
  }

}