package com.monadial.canary.server.nonce.actor.http.resource.v1

import akka.actor.typed.ActorRef
import akka.http.scaladsl.server.Route
import com.monadial.canary.server.nonce.actor.http.resource.Resource
import com.monadial.canary.server.nonce.actor.nonce.NonceActor
import akka.http.scaladsl.server.Directives._

final class NewNonceResource(
    nonceActor: ActorRef[NonceActor.Message]
) extends Resource {
  override val routes: Route = get {
    complete(nonceActor.path.address.toString)
  }
}

object NewNonceResource {
  def apply(nonceActor: ActorRef[NonceActor.Message]): Route =
    new NewNonceResource(nonceActor).routes
}
