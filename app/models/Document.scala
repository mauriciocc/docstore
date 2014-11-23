package models

import java.sql.Timestamp
import java.util.Date

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._
import utils.JsonFormats

case class Document(name: String,
                    databaseFileId: Option[Long],
                    customerId: Long,
                    dueDate: Option[Date],
                    createdAt: Timestamp = new Timestamp(new Date().getTime),
                    override val id: Long = 0) extends ActiveRecord {
  lazy val customer = belongsTo[Customer]
  lazy val databaseFile = belongsTo[DatabaseFile]
}

object Document extends ActiveRecordCompanion[Document] with PlayFormSupport[Document] {

  implicit val timestampFormat = JsonFormats.timestampFormat
  implicit val format = Json.format[Document]

  def forUser(id: Long) = {
    Customer.forUser(id).flatten(cus =>
      cus.documents.toList
    )
  }

  def lastDownloadFor(doc: Document) = {
    try {
      doc.databaseFile.get.databaseFileDownload.limit(1).orderBy(_.downloadedAt desc).headOption
    }
    catch {
      case e: Exception => None
    }
  }

  def forCustomer(id: Long) = {
    Document
      .joins[Customer]((doc, cust) => doc.customerId === cust.id)
      .where((doc, cust) => doc.customerId === id)
      .select((doc, cust) => (doc, cust))
      .map(r => (r._1, r._2, lastDownloadFor(r._1)))
      .toList
  }

  def databaseFileFor(id: Long): Option[DatabaseFile] = {
    find(id) match {
      case Some(document) =>
        document.databaseFile.toOption
    }
  }
}