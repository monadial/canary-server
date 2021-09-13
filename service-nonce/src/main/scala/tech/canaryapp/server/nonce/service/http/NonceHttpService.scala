package tech.canaryapp.server.nonce.service.http

import akka.http.scaladsl.server.Route
import tech.canaryapp.server.nonce.config.NonceHttpServiceConfig
import tech.canaryapp.server.util.reusable.actor.http.service.HttpService
import akka.http.scaladsl.server.Directives._

import scala.concurrent.duration.FiniteDuration

final class NonceHttpService(val config: NonceHttpServiceConfig) extends HttpService[NonceHttpServiceConfig] {
  override val routes: Route =  get {
    complete("Nonce Service: " + config.toString)
  }
}
