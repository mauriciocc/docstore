package models

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, JsPath}
import play.api.Play.current

case class Customer(id: Option[Long], name: String, officeId: Long)

object Customer {
  implicit val format: Format[Customer] = (
    (JsPath \ "id").formatNullable[Long] and
      (JsPath \ "name").format[String](minLength[String](1)) and
      (JsPath \ "officeId").format[Long]
    )(Customer.apply, unlift(Customer.unapply))
}

object CustomerRepo extends Repository[Customer] {

  val parser: RowParser[Customer] = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Long]("office_id") map {
      case id ~ name ~ office_id =>
        Customer(id, name, office_id)
    }
  }

  val parsers: ResultSetParser[List[Customer]] = {
    parser.*
  }

  override def save(obj: Customer): Customer = {
    DB.withTransaction(implicit con =>
      obj.id match {
        case Some(id) =>
          SQL"update customer set name = ${obj.name}, office_id = ${obj.officeId} where id = ${obj.id}".executeUpdate()
          obj
        case None =>
          val id: Option[Long] =
            SQL"insert into customer (name, office_id) values (${obj.name}, ${obj.officeId})".executeInsert()
          obj.copy(id = id)
      }
    )
  }

  override def findOne(id: Long): Option[Customer] = {
    DB.withConnection(implicit con =>
      SQL"select * from customer where id = $id".as(parser.singleOpt)
    )
  }

  override def findAll(): List[Customer] = {
    DB.withConnection(implicit con =>
      SQL"select * from customer".as(parsers)
    )
  }

  def findAllForUser(userId: Long): List[Customer] = {
    DB.withConnection(implicit con =>
      SQL"select * from customer where office_id in (select id from office where organization_id in (select id from organization where account_id in (select id from user_account where id = $userId)))".as(parsers)
    )
  }

  override def remove(id: Long): Unit = {
    DB.withTransaction(implicit con =>
      SQL"delete from customer where id = $id".executeUpdate()
    )
  }

}