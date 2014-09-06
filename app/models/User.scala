package models

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

case class User(id: Option[Long], email: String, name: String, displayName: String, password: String)

object User {

  implicit val userFormat: Format[User] = (
    (JsPath \ "id").formatNullable[Long] and
    (JsPath \ "email").format[String](email) and
    (JsPath \ "name").format[String](minLength[String](1)) and
    (JsPath \ "displayName").format[String] and
    (JsPath \ "password").format[String](minLength[String](6))
    )(User.apply, unlift(User.unapply))

}
