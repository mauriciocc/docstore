package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index(path: String) = Action {
    Ok(views.html.main())
  }

  def jsRoutes = Action { implicit request =>
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.Users.save,
        routes.javascript.Users.createAccount,
        routes.javascript.Users.view
      )
    ).as("text/javascript")
  }

}