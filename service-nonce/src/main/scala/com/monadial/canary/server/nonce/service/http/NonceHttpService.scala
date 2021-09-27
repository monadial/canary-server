package com.monadial.canary.server.nonce.service.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.monadial.canary.server.nonce.config.NonceHttpServiceConfig
import com.monadial.canary.server.util.reusable.actor.http.service.HttpService

import scala.concurrent.duration.FiniteDuration

final class NonceHttpService(val config: NonceHttpServiceConfig) extends HttpService[NonceHttpServiceConfig] {
  override val routes: Route =  get {
    complete("Nonce Service: " + config.toString)
  }
}
