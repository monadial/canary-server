package com.monadial.canary.server.auth.config

import java.util.concurrent.TimeUnit
import akka.kafka.{CommitterSettings, ConsumerSettings, ProducerSettings}
import com.monadial.canary.server.service.config.Config
import com.typesafe.config.{Config => TConfig}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, ByteArraySerializer}

import scala.concurrent.duration.FiniteDuration

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class AuthServiceConfigImpl(systemConfig: Config) extends AuthServiceConfig {

  import com.monadial.canary.server.util.patterns.config.Implicits._

  private val rootConfig = systemConfig.rootConfig

  private lazy val serviceRoot: TConfig =
    rootConfig.getConfig("canary.auth")

  override val gracefulShutdownTimeout: FiniteDuration =
    FiniteDuration(serviceRoot.getDuration("graceful-shutdown-timeout").toMillis, TimeUnit.MILLISECONDS)

  override val idleTimeout: FiniteDuration =
    FiniteDuration(serviceRoot.getDuration("idle-timeout").toMillis, TimeUnit.MILLISECONDS)

  override val bufferSize: Int =
    serviceRoot.getInt("buffer-size")

  override val parallelism: Int =
    serviceRoot.getInt("parallelism")

  override val maxBatchSize: Int =
    serviceRoot.getInt("max-batch-size")

  override val maxBatchAge: FiniteDuration =
    FiniteDuration(serviceRoot.getDuration("max-batch-age").toMillis, TimeUnit.MILLISECONDS)

  override val database: DatabaseConfig = new DatabaseConfig {
    private lazy val databaseRoot: TConfig = serviceRoot.getConfig("database")

    override val host: String = databaseRoot.getString("host")
    override val port: Int = databaseRoot.getInt("port")
    override val user: String = databaseRoot.getString("user")
    override val pass: String = databaseRoot.getString("pass")
    override val name: String = databaseRoot.getString("name")
    override val autoCommit: Option[Boolean] = databaseRoot.getBooleanOption("auto-commit")
    override val threadPool: Option[Int] = databaseRoot.getIntOption("thread-pool")
  }

  override val httpService: HttpServerConfig = new HttpServerConfig {
    private lazy val httpServiceRoot: TConfig = serviceRoot.getConfig("http-server")

    override val interface: String = httpServiceRoot.getString("interface")
    override val port: Int = httpServiceRoot.getInt("port")
  }

  override val kafkaConsumerSettings: ConsumerSettings[Array[Byte], Array[Byte]] =
    ConsumerSettings(systemConfig.kafkaConfig.kafkaConsumerSettings, new ByteArrayDeserializer, new ByteArrayDeserializer)
      .withBootstrapServers(systemConfig.kafkaConfig.bootstrapServers.mkString(","))

  override val kafkaProducerSettings: ProducerSettings[Array[Byte], Array[Byte]] =
    ProducerSettings(systemConfig.kafkaConfig.kafkaProducerSettings, new ByteArraySerializer, new ByteArraySerializer)
      .withBootstrapServers(systemConfig.kafkaConfig.bootstrapServers.mkString(","))

  override val kafkaCommitterSettings: CommitterSettings =
    CommitterSettings(systemConfig.kafkaConfig.kafkaCommitterSettings)
}
