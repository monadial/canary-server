package tech.canaryapp.server.auth.config

import tech.canaryapp.server.service.config.{KafkaConfig, ServiceConfig}

import scala.concurrent.duration.FiniteDuration
import akka.kafka.ConsumerSettings
import akka.kafka.ProducerSettings

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
