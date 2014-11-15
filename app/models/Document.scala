package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.json._

case class Document(name: String, databaseFileId: Option[Long], customerId: Long, override val id: Long = 0) extends ActiveRecord {
  lazy val customer = belongsTo[Customer]
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

}

object DocumentRepo {
  def databaseFileFor(documentId: Long): Option[DatabaseFile] = {
    DB.withConnection(
      implicit con => {
        Document.find(documentId) match {
          case Some(doc) =>
            DatabaseFileRepo.findOne(doc.databaseFileId.get)
        }
      }
    )
  }

}

