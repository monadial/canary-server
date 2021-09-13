package tech.canaryapp.server.util.reusable.actor.http.service

import akka.http.scaladsl.server.Route
import tech.canaryapp.server.util.reusable.actor.http.config.HttpServerConfig

import scala.concurrent.duration.FiniteDuration

trait HttpService[CFG <: HttpServerConfig] {
  val config: CFG
  val routes: Route
}
