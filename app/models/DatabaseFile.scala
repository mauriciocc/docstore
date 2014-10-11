package models

case class DatabaseFile(id: Option[Long], name: String, content: Array[Byte]) extends Model[Option[Long]]
