package models

case class EmailAlreadyInUseException(email: String) extends Exception
