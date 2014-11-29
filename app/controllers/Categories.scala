package controllers

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import models._
import play.api.libs.json._


object Categories extends Crud[Category] {

  override val companion = Category
  override implicit val format = Category.format

  def findAll() = HasToken() { _ => currentUserId => implicit request =>
    Ok(
      Json.toJson(
        Category.all.toList
      )
    );
  }

}