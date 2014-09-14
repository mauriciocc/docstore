package models

case class EmailAlreadyInUseException(email: String, message: String = null, cause: Throwable = null) extends Exception(message, cause)
