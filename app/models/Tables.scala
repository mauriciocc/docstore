package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

object Tables extends ActiveRecordTables with PlaySupport {
  val users = table[User]("user_account")
  val accounts = table[Account]("account")
  val organizations = table[Organization]("organization")
  val offices = table[Office]("office")
  val customers = table[Customer]("customer")
  val document = table[Document]("document")
}