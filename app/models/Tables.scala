package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

object Tables extends ActiveRecordTables with PlaySupport {
  val users = table[User]("user_account")
  val accounts = table[Account]("account")
  val organizations = table[Organization]("organization")
  val offices = table[Office]("office")
  val customers = table[Customer]("customer")
  val documents = table[Document]("document")
  val databaseFiles = table[DatabaseFile]("database_file")
  val databaseFileDownloads = table[DatabaseFileDownload]("database_file_download")
  val notifications = table[Notification]("notification")
}