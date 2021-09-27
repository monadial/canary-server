package com.monadial.canary.server.nonce.config

import com.monadial.canary.server.util.reusable.actor.http.config.HttpServerConfig

import scala.concurrent.duration.FiniteDuration

case class NonceHttpServiceConfig(
  gracefulShutdownTimeout: FiniteDuration,
  interface: String,
  port: Int
) extends HttpServerConfig
