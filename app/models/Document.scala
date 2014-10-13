package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, JsPath}

case class Document(id: Option[Long], name: String, databaseFileId: Option[Long], customerId: Long) extends Model[Option[Long]]

object Document {
  implicit val format: Format[Document] = (
    (JsPath \ "id").formatNullable[Long] and
      (JsPath \ "name").format[String](minLength[String](1)) and
      (JsPath \ "databaseFileId").formatNullable[Long] and
      (JsPath \ "customerId").format[Long]
    )(Document.apply, unlift(Document.unapply))
}

object DocumentRepo extends Repository[Document] {

  val parser: RowParser[Document] = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Option[Long]]("database_file_id") ~
      get[Long]("customer_id") map {
      case id ~ name ~ database_file_id ~ customer_id =>
        Document(id, name, database_file_id, customer_id)
    }
  }

  val parsers: ResultSetParser[List[Document]] = {
    parser.*
  }

  override def save(obj: Document): Document = {
    DB.withTransaction(implicit con =>
      obj.id match {
        case Some(id) =>
          SQL"update document set name = ${obj.name}, database_file_id = ${obj.databaseFileId}, customer_id = ${obj.customerId} where id = ${obj.id}".executeUpdate()
          obj
        case None =>
          val id: Option[Long] =
            SQL"insert into document (name, database_file_id, customer_id) values (${obj.name}, ${obj.databaseFileId}, ${obj.customerId})".executeInsert()
          obj.copy(id = id)
      }
    )
  }

  override def findOne(id: Long): Option[Document] = {
    DB.withConnection(implicit con =>
      SQL"select * from document where id = $id".as(parser.singleOpt)
    )
  }

  override def findAll(): List[Document] = {
    DB.withConnection(implicit con =>
      SQL"select * from document".as(parsers)
    )
  }

  def findAllForCustomer(customerId: Long): List[Document] = {
    DB.withConnection(implicit con =>
      SQL"select * from document where customer_id = $customerId".as(parsers)
    )
  }

  override def remove(id: Long): Unit = {
    DB.withTransaction(
      implicit con => {
        SQL"delete from database_file where id = (select database_file_id from document where id = $id)".executeUpdate()
        SQL"delete from document where id = $id".executeUpdate()
      }
    )
  }

  def databaseFileFor(documentId: Long): Option[DatabaseFile] = {
    DB.withConnection(
      implicit con => {
        val doc = findOne(documentId)
        DatabaseFileRepo.findOne(doc.get.databaseFileId.get)
      }
    )
  }

}

