package global 

import play.api._
import play.api.mvc._

object Global extends GlobalSettings {

  override def onStart(app: Application) = {
  	controllers.EventStream.start()
  }

  
}