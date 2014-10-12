package models

import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.sql.PreparedStatement

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, JsPath}
import play.api.Play.current

case class DatabaseFile(id: Option[Long], name: String, contentType: String, content: Array[Byte]) extends Model[Option[Long]]

object DatabaseFileRepo extends Repository[DatabaseFile] {

  implicit object byteArrayToStatement extends ToStatement[Array[Byte]] {
    def set(s: PreparedStatement, i: Int, array: Array[Byte]): Unit = {
      s.setBinaryStream(i, new ByteArrayInputStream(array), array.length)
    }
  }

  implicit def rowToByteArray: Column[Array[Byte]] = {
    Column.nonNull[Array[Byte]] { (value, meta) =>
      val MetaDataItem(qualified, nullable, clazz) = meta
      value match {
        case bytes: Array[Byte] => Right(bytes)
        case _ => Left(TypeDoesNotMatch("..."))
      }
    }
  }

  val parser: RowParser[DatabaseFile] = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[String]("content_type") ~
      get[Array[Byte]]("content") map {
      case id ~ name ~ content_type ~ content =>
        DatabaseFile(id, name, content_type, content)
    }
  }

  val parsers: ResultSetParser[List[DatabaseFile]] = {
    parser.*
  }

  override def save(obj: DatabaseFile): DatabaseFile = {
    DB.withTransaction(implicit con =>
      obj.id match {
        case Some(id) =>
          SQL"update database_file set name = ${obj.name}, content_type = ${obj.contentType}, content = ${obj.content} where id = ${obj.id}".executeUpdate()
          obj
        case None =>
          val id: Option[Long] =
            SQL"insert into database_file (name, content_type, content) values (${obj.name}, ${obj.contentType}, ${obj.content})".executeInsert()
          obj.copy(id = id)
      }
    )
  }

  override def findOne(id: Long): Option[DatabaseFile] = {
    DB.withConnection(implicit con =>
      SQL"select * from database_file where id = $id".as(parser.singleOpt)
    )
  }

  override def findAll(): List[DatabaseFile] = {
    DB.withConnection(implicit con =>
      SQL"select * from database_file".as(parsers)
    )
  }

  def findAllForCustomer(customerId: Long): List[DatabaseFile] = {
    DB.withConnection(implicit con =>
      SQL"select * from database_file where customer_id = $customerId".as(parsers)
    )
  }

  override def remove(id: Long): Unit = {
    DB.withTransaction(implicit con =>
      SQL"delete from database_file where id = $id".executeUpdate()
    )
  }

}