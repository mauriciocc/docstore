package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json.Json

case class User(@Unique email: String,
                @Unique name: String,
                displayName: Option[String],
                @Length(min = 6) password: String,
                override val id: Long = 0) extends ActiveRecord {
  lazy val account = hasOne[Account]
}

object User extends ActiveRecordCompanion[User] with PlayFormSupport[User] {
  implicit val format = Json.format[User]
}