package models

import java.sql.Connection

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class Account(id: Option[Long], name: String, ownerId: Long)

object Account {
  implicit val accountFormat: Format[Account] = (
    (JsPath \ "id").formatNullable[Long] and
      (JsPath \ "name").format[String](email) and
      (JsPath \ "ownerId").format[Long]
    )(Account.apply, unlift(Account.unapply))
}

object AccountRepo extends Repository[Account] {

  val accountParser: RowParser[Account] = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Long]("owner_id") map {
      case id ~ name ~ owner_id =>
        Account(id, name, owner_id)
    }
  }

  val accountsParser: ResultSetParser[List[Account]] = {
    accountParser.*
  }

  def saveNoTransaction(obj: Account)(implicit connection: Connection): Account = {
    obj.id match {
      case Some(id) =>
        SQL"update account set name = ${obj.name}, owner_id = ${obj.ownerId} where id = ${obj.id}".executeUpdate()
        obj
      case None =>
        val id: Option[Long] = SQL"insert into account (name, owner_id) values (${obj.name}, ${obj.ownerId})".executeInsert()
        obj.copy(id = id)
    }
  }

  override def save(obj: Account): Account = {
    DB.withTransaction(implicit con =>
      saveNoTransaction(obj)
    )
  }

  override def findOne(id: Long): Option[Account] = {
    DB.withConnection(implicit con =>
      SQL"select * from account where id = $id".as(accountParser.singleOpt)
    )
  }

  override def findAll(): List[Account] = {
    DB.withConnection(implicit con =>
      SQL"select * from account".as(accountsParser)
    )
  }

  def findAll(userId: Long): List[Account] = {
    DB.withConnection(implicit con =>
      SQL"select * from account where owner_id = $userId".as(accountsParser)
    )
  }

  override def remove(id: Long): Unit = {
    DB.withTransaction(implicit con =>
      SQL"delete from account where id = $id".executeUpdate()
    )
  }

}