package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, Format, JsPath}
import play.api.Play.current

case class Office(id: Option[Long], name: String, organizationId: Long)

object Office {
  implicit val officeFormat: Format[Office] = Json.format[Office]
}

object OfficeRepo extends Repository[Office] {

  val parser: RowParser[Office] = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Long]("organization_id") map {
      case id ~ name ~ organization_id =>
        Office(id, name, organization_id)
    }
  }

  val parsers: ResultSetParser[List[Office]] = {
    parser.*
  }

  override def save(obj: Office): Office = {
    DB.withTransaction(implicit con =>
      obj.id match {
        case Some(id) =>
          SQL"update office set name = ${obj.name}, organization_id = ${obj.organizationId} where id = ${obj.id}".executeUpdate()
          obj
        case None =>
          val id: Option[Long] =
            SQL"insert into office (name, organization_id) values (${obj.name}, ${obj.organizationId})".executeInsert()
          obj.copy(id = id)
      }
    )
  }

  override def findOne(id: Long): Option[Office] = {
    DB.withConnection(implicit con =>
      SQL"select * from office where id = $id".as(parser.singleOpt)
    )
  }

  override def findAll(): List[Office] = {
    DB.withConnection(implicit con =>
      SQL"select * from office".as(parsers)
    )
  }

  def findAllForUser(userId: Long): List[Office] = {
    DB.withConnection(implicit con =>
      SQL"select * from office where organization_id in (select id from organization where account_id in (select id from user_account where id = $userId))".as(parsers)
    )
  }

  def findAll(organizationId: Long): List[Office] = {
    DB.withConnection(implicit con =>
      SQL"select * from office where organizationId = $organizationId".as(parsers)
    )
  }

  override def remove(id: Long): Unit = {
    DB.withTransaction(implicit con =>
      SQL"delete from office where id = $id".executeUpdate()
    )
  }
}