package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._

case class Office(name: String, organizationId: Long, override val id: Long = 0) extends ActiveRecord {
  lazy val organization = belongsTo[Organization]
  lazy val customers = hasMany[Customer]
}

object Office extends ActiveRecordCompanion[Office] with PlayFormSupport[Office] {

  implicit val format = Json.format[Office]

  def forUser(id: Long) = {
    Organization.forUser(id).flatten( org =>
      org.offices.toList
    )
  }

}