package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import play.api.libs.json._

case class Category(@Required name: String,
                    override val id: Long = 0) extends ActiveRecord

object Category extends ActiveRecordCompanion[Category] with PlayFormSupport[Category] {
  implicit val format = Json.format[Category]
}