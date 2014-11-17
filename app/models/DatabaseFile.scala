package models

import com.github.aselab.activerecord.{ActiveRecord, ActiveRecordCompanion, PlayFormSupport}

case class DatabaseFile(name: String,
                        contentType: String,
                        content: Array[Byte],
                        override val id: Long = 0) extends ActiveRecord {
  lazy val document = hasOne[Document]
}

object DatabaseFile extends ActiveRecordCompanion[DatabaseFile] with PlayFormSupport[DatabaseFile]
