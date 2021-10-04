package com.monadial.canary.server.nonce.service.http

import akka.actor.typed.ActorRef
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.monadial.canary.server.nonce.actor.nonce.NonceActor

final case class NonceHttpService(private val nonceActor: ActorRef[NonceActor.Message]) extends HttpService {

  override def routes: Route = complete(nonceActor.path.address.toString)
}
