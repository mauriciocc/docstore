package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._

case class Customer(name: String, officeId: Long, override val id: Long = 0) extends ActiveRecord {
  lazy val office = belongsTo[Office]
  lazy val documents = hasMany[Document]
}

object Customer extends ActiveRecordCompanion[Customer] with PlayFormSupport[Customer] {
  def responsiblesUsers(id: Long): Seq[Long] = {
    Customer.find(id) match {
      case Some(c) =>
        User.all.map(u => u.id).toSeq
      case None =>
        Seq.empty
    }
  }

  implicit val format = Json.format[Customer]

  def forUser(id: Long) = {
    Office.forUser(id).flatten( off =>
      off.customers.orderBy(_.name).toList
    )
  }

}