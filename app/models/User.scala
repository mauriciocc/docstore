package models

case class User(id: Option[Long], email: String, name: String, password: String)