package models

trait Repository[T] {
  def save(obj: T): T
  def remove(id: Long)
  def findOne(id: Long): Option[T]
  def findAll(): List[T]
}
