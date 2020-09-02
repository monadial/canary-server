package tech.canaryapp.server.auth.config

import akka.kafka.{ConsumerSettings, ProducerSettings}
import tech.canaryapp.server.service.config.ServiceConfig

import scala.concurrent.duration.FiniteDuration

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait AuthServiceConfig extends ServiceConfig {
  val gracefulShutdownTimeout: FiniteDuration

  val database: DatabaseConfig

  val httpService: HttpServerConfig

  val kafkaConsumerSettings: ConsumerSettings[Array[Byte], Array[Byte]]

  val kafkaProducerSettings: ProducerSettings[Array[Byte], Array[Byte]]
}
