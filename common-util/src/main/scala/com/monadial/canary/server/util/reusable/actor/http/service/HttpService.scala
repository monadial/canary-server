package com.monadial.canary.server.util.reusable.actor.http.service

import akka.http.scaladsl.server.Route
import com.monadial.canary.server.util.reusable.actor.http.config.HttpServerConfig

import scala.concurrent.duration.FiniteDuration

trait HttpService[CFG <: HttpServerConfig] {
  val config: CFG
  val routes: Route
}
