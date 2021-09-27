package com.monadial.canary.server.util.reusable.actor.http.config

import scala.concurrent.duration.FiniteDuration

trait HttpServerConfig {
  val gracefulShutdownTimeout: FiniteDuration
  val interface: String
  val port: Int

  override def toString: String = interface + ":" + port.toString
}
