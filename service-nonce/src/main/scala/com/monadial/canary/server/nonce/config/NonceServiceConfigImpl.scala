package com.monadial.canary.server.nonce.config

import com.monadial.canary.server.service.config.Config
import com.typesafe.config.{Config => TConfig}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

final case class NonceServiceConfigImpl(systemConfig: Config) extends NonceServiceConfig {

  import com.monadial.canary.server.util.patterns.config.Implicits._

  private val rootConfig = systemConfig.rootConfig

  private lazy val serviceRoot: TConfig =
    rootConfig.getConfig("canary.nonce")

  override val gracefulShutdownTimeout: FiniteDuration =
    FiniteDuration(serviceRoot.getDuration("graceful-shutdown-timeout").toMillis, TimeUnit.MILLISECONDS)

  override val idleTimeout: FiniteDuration =
    FiniteDuration(serviceRoot.getDuration("idle-timeout").toMillis, TimeUnit.MILLISECONDS)

  override val httpServiceConfig: NonceHttpServiceConfig = {
    lazy val httpServiceRoot: TConfig = serviceRoot.getConfig("http-server")

    NonceHttpServiceConfig(
      gracefulShutdownTimeout,
      httpServiceRoot.getString("interface"),
      httpServiceRoot.getInt("port")
    )
  }

}
