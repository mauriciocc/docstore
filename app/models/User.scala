package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class User(id: Option[Long], email: String, name: String, displayName: Option[String], password: String)

object User {

  implicit val userFormat: Format[User] = (
    (JsPath \ "id").formatNullable[Long] and
      (JsPath \ "email").format[String](email) and
      (JsPath \ "name").format[String](minLength[String](1)) and
      (JsPath \ "displayName").formatNullable[String] and
      (JsPath \ "password").format[String](minLength[String](6))
    )(User.apply, unlift(User.unapply))

}

object UserRepo extends Repository[User] {

  val table = "user_account"

  val userParser: RowParser[User] = {
    get[Option[Long]]("id") ~
      get[String]("email") ~
      get[String]("name") ~
      get[Option[String]]("display_name") ~
      get[String]("password") map {
      case id ~ email ~ name ~ display_name ~ password =>
        User(id, email, name, display_name, password)
    }
  }

  val usersParser: ResultSetParser[List[User]] = {
    userParser.*
  }

  override def save(obj: User): User = {
    DB.withTransaction(implicit con =>
      obj.id match {
        case Some(id) =>
          SQL"update $table set name = ${obj.name},display_name = ${obj.displayName},email = ${obj.email},password = ${obj.password}) where id = ${obj.id}".executeUpdate()
          obj
        case None =>
          if (isEmailInUse(obj.email)) {
            throw new EmailAlreadyInUseException(obj.email, s"Email '${obj.email}' already in use")
          }
          val id: Option[Long] = SQL"insert into $table (name, display_name, email, password) values (${obj.name}, ${obj.displayName}, ${obj.email}, ${obj.password})".executeInsert()
          AccountRepo.save(Account(None, obj.name, id.get))
          obj.copy(id = id);
      }
    )
  }

  override def findOne(id: Long): Option[User] = {
    DB.withConnection(implicit con =>
      SQL"select * from $table where id = $id".as(userParser.singleOpt)
    )
  }

  override def findAll(): List[User] = {
    DB.withConnection(implicit con =>
      SQL"select * from $table".as(usersParser)
    )
  }

  override def remove(id: Long): Unit = {
    DB.withTransaction(implicit con =>
      SQL"delete from user where id = $id".executeUpdate()
    )
  }

  def isEmailInUse(email: String): Boolean = {
    DB.withConnection(implicit con =>
      SQL"select count(*) from $table where email = $email".as(scalar[Long].single) > 0
    )
  }

  def findByUserNameAndPassword(userName: String, password: String): Option[User] = {
    DB.withConnection(implicit con =>
      SQL"select * from $table where name = $userName and password = $password".as(userParser.singleOpt)
    )
  }

  def findByEmailAndPassword(email: String, password: String): Option[User] = {
    DB.withConnection(implicit con =>
      SQL"select * from $table where email = $email and password = $password".as(userParser.singleOpt)
    )
  }

}
