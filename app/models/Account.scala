package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json
import play.api.libs.json.Json

case class Account(@Required name: String,
                    ownerId: Long) extends ActiveRecord {
  lazy val owner = belongsTo[User]
}

object Account extends ActiveRecordCompanion[Account] with PlayFormSupport[Account] {
  implicit val format: json.Format[Account] = Json.format[Account]
}