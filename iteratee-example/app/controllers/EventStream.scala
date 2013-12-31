package controllers

import play.api.libs.iteratee.Enumerator
import play.api.libs.concurrent.Promise
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Iteratee
import scala.concurrent.Future
import java.util.concurrent.TimeUnit
import scala.util.Random
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json.Json

case class Event(tpe: String, e: Int)

case object Event {
  implicit val format = Json.format[Event]
}

object EventStream {

  def start() = {

  	val rawStream = Enumerator.generateM {
  		Promise.timeout(Some(Random.nextInt), 500 milliseconds)
  	}

  	val (broadcasting, _) = Concurrent.broadcast(rawStream)

  	val slowConsumer: Iteratee[Int, Unit] = Iteratee.foldM(()) { (a, e) =>
       Promise.timeout(fireSlowEvent(e), 5000 milliseconds)
  	}

  	val slowConsumer1 = Concurrent.dropInputIfNotReady(50, TimeUnit.MILLISECONDS).apply(slowConsumer) 

  	val fastConsumer: Iteratee[Int, Unit] = Iteratee.foldM(()) { (a, e) =>
       Promise.timeout(fireFastEvent(e), 50 milliseconds)
  	}

  	broadcasting.apply(slowConsumer1)
  	broadcasting.apply(fastConsumer)
  }

  def fireFastEvent(e: Int): Unit = {
    EventPublisher.channel.push(Json.toJson(Event("Fast", e)))
  }

  def fireSlowEvent(e: Int): Unit = EventPublisher.channel.push(Json.toJson(Event("Slow", e)))









}