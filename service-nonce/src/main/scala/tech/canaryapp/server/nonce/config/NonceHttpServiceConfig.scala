package tech.canaryapp.server.nonce.config

import tech.canaryapp.server.util.reusable.actor.http.config.HttpServerConfig

import scala.concurrent.duration.FiniteDuration

case class NonceHttpServiceConfig(
  gracefulShutdownTimeout: FiniteDuration,
  interface: String,
  port: Int
) extends HttpServerConfig
