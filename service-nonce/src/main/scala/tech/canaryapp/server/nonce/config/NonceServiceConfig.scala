package tech.canaryapp.server.nonce.config

import tech.canaryapp.server.service.config.ServiceConfig

import scala.concurrent.duration.FiniteDuration

trait NonceServiceConfig extends ServiceConfig {

  val gracefulShutdownTimeout: FiniteDuration

  val idleTimeout: FiniteDuration

  val httpServiceConfig: NonceHttpServiceConfig
}
