package models

import java.util.Date

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._

case class Document(name: String,
                    databaseFileId: Option[Long],
                    customerId: Long,
                    dueDate: Option[Date],
                    override val id: Long = 0) extends ActiveRecord {
  lazy val customer = belongsTo[Customer]
  lazy val databaseFile = belongsTo[DatabaseFile]
}

object Document extends ActiveRecordCompanion[Document] with PlayFormSupport[Document] {

  implicit val format = Json.format[Document]

  def forUser(id: Long) = {
    Customer.forUser(id).flatten(cus =>
      cus.documents.toList
    )
  }

  def forCustomer(id: Long) = {
    Document.where(_.customerId === id).toList
  }

  def databaseFileFor(id: Long): Option[DatabaseFile] = {
    find(id) match {
      case Some(document) =>
        document.databaseFile.toOption
    }
  }
}