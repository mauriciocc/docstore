package models

case class User(id: Long, email: String, name: String, password: String) extends Model[Long] {
}
