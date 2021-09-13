package tech.canaryapp.server.nonce.config

import com.typesafe.config.{Config => TConfig}
import tech.canaryapp.server.service.config.Config
import tech.canaryapp.server.util.patterns.config.{Implicits => TConfigImplicits}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

final case class NonceServiceConfigImpl(systemConfig: Config) extends NonceServiceConfig {

  import TConfigImplicits._

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
