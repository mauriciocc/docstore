package controllers

import com.github.aselab.activerecord.{ActiveRecordCompanion, PlayFormSupport}
import models._
import play.api.libs.json._


object Customers extends Crud[Customer] {

  override val companion: ActiveRecordCompanion[Customer] with PlayFormSupport[Customer] = Customer
  override implicit val format: Format[Customer] = Customer.format

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(Json.toJson(Customer.forUser(currentUserId)));
  }

}