package models

import java.sql.Timestamp
import java.util.Date

import com.github.aselab.activerecord.{ActiveRecord, ActiveRecordCompanion, PlayFormSupport}

case class DatabaseFileDownload(userId: Long,
                                databaseFileId: Long,
                                downloadedAt: Timestamp = new Timestamp(new Date().getTime),
                                override val id: Long = 0) extends ActiveRecord {
  lazy val databaseFile = belongsTo[DatabaseFile]
  lazy val user = belongsTo[User]
}

object DatabaseFileDownload extends ActiveRecordCompanion[DatabaseFileDownload] with PlayFormSupport[DatabaseFileDownload]
