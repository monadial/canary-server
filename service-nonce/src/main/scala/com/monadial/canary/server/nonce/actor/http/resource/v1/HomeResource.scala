package com.monadial.canary.server.nonce.actor.http.resource.v1

import akka.http.scaladsl.server.Route
import com.monadial.canary.server.nonce.actor.http.resource.Resource
import akka.http.scaladsl.server.Directives._

final class HomeResource extends Resource {
  override val routes: Route = get {
    complete("hello world")
  }
}

object HomeResource {
  def apply(): HomeResource = new HomeResource()
}
