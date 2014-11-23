package models

import com.github.aselab.activerecord.{ActiveRecord, ActiveRecordCompanion, PlayFormSupport}
import play.api.libs.json._

case class DatabaseFile(name: String,
                        contentType: String,
                        content: Array[Byte],
                        size: Int,
                        override val id: Long = 0) extends ActiveRecord {
  lazy val document = hasOne[Document]
  lazy val databaseFileDownload = hasMany[DatabaseFileDownload]
}

object DatabaseFile extends ActiveRecordCompanion[DatabaseFile] with PlayFormSupport[DatabaseFile] {
  implicit val ignoreByteArray = new Format[DatabaseFile] {
    override def reads(json: JsValue): JsResult[DatabaseFile] = {
      JsSuccess(
        DatabaseFile(
          json.\("name").as[String],
          json.\("contentType").as[String],
          Array.empty, json.\("size").as[Int],
          json.\("id").as[Long]
        )
      )
    }

    override def writes(o: DatabaseFile): JsValue = {
      Json.obj("id" -> o.id, "name" -> o.name, "contentType" -> o.contentType, "size" -> o.size)
    }

  }
}
