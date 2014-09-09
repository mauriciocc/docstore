package models

trait Repository[T] {
  def save(obj: T): T
  def remove(obj: T)
  def findOne(id: Long): T
  def findAll(): List[T]
}
