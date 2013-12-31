package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.JsValue
import play.api.libs.iteratee.Iteratee
import play.api.libs.iteratee.Enumeratee
import play.api.libs.iteratee.Concurrent

object Application extends Controller {
  
  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }
  
  def events = WebSocket.using { rh =>
     (EventPublisher.in, EventPublisher.out) 
  }
  
}


object EventPublisher {
  val in = Iteratee.ignore[JsValue]  
  val (out, channel) = Concurrent.broadcast[JsValue]  
}