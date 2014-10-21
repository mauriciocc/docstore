package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, Format, JsPath}
import play.api.Play.current

case class Organization(id: Option[Long], name: String, accountId: Long) extends Model[Option[Long]]

object Organization {
  implicit val organizationFormat: Format[Organization] = Json.format[Organization]
}

object OrganizationRepo extends Repository[Organization] {

  val organizationParser: RowParser[Organization] = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Long]("account_id") map {
      case id ~ name ~ account_id =>
        Organization(id, name, account_id)
    }
  }

  val organizationsParser: ResultSetParser[List[Organization]] = {
    organizationParser.*
  }

  override def save(obj: Organization): Organization = {
    DB.withTransaction(implicit con =>
      obj.id match {
        case Some(id) =>
          SQL"update organization set name = ${obj.name}, account_id = ${obj.accountId} where id = ${obj.id}".executeUpdate()
          obj
        case None =>
          val id: Option[Long] =
            SQL"insert into organization (name, account_id) values (${obj.name}, ${obj.accountId})".executeInsert()
          obj.copy(id = id)
      }
    )
  }

  override def findOne(id: Long): Option[Organization] = {
    DB.withConnection(implicit con =>
      SQL"select * from organization where id = $id".as(organizationParser.singleOpt)
    )
  }

  override def findAll(): List[Organization] = {
    DB.withConnection(implicit con =>
      SQL"select * from organization".as(organizationsParser)
    )
  }

  def findAll(userId: Long): List[Organization] = {
    DB.withConnection(implicit con =>
      SQL"select * from organization where account_id in (select id from user_account where id = $userId)".as(organizationsParser)
    )
  }

  override def remove(id: Long): Unit = {
    DB.withTransaction(implicit con =>
      SQL"delete from organization where id = $id".executeUpdate()
    )
  }

}