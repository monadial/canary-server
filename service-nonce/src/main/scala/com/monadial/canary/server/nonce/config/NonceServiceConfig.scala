package com.monadial.canary.server.nonce.config

import com.monadial.canary.server.service.config.ServiceConfig

import scala.concurrent.duration.FiniteDuration

trait NonceServiceConfig extends ServiceConfig {

  val gracefulShutdownTimeout: FiniteDuration

  val idleTimeout: FiniteDuration

  val httpServiceConfig: NonceHttpServiceConfig
}
