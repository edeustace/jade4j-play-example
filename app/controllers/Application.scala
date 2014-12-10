package controllers

import play.api.mvc._

object Application extends Controller with Jade {

  def index = Action {
    Ok(renderJade("index", Map("msg" -> "Your new application is ready.")))
  }

}