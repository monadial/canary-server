package com.monadial.canary.server.nonce.actor.http.resource

import akka.http.scaladsl.server.Route

trait Resource {

  val routes: Route
}
