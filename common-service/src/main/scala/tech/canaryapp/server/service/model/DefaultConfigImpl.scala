package tech.canaryapp.server.service.model

import tech.canaryapp.server.service.config.{Config, KafkaConfig, ServiceConfig, ServiceConfigFactory}
import com.typesafe.config.{ConfigFactory, Config => TConfig}
import scala.jdk.CollectionConverters._
import scala.util.Try

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class DefaultConfigImpl(rootConfig: TConfig) extends Config {

  override val logbackXml: String =
    rootConfig.getString("logback.xml")

  override val kafkaConfig: KafkaConfig = new KafkaConfig {
    val kc: TConfig = rootConfig.getConfig("kafka")

    override val bootstrapServers: List[String] =
      kc.getStringList("bootstrap-servers").asScala.toList

    override val messagesTopicName: String =
      kc.getString("topics.messages.name")

    override val kafkaConsumerSettings: TConfig =
      kc.getConfig("consumer-settings")

    override val kafkaProducerSettings: TConfig =
      kc.getConfig("producer-settings")
  }
}

object DefaultConfigImpl {

  val rootConfig: TConfig => Try[TConfig] = (config: TConfig) => Try[TConfig] {
    (config :: Nil)
      .foldLeft(ConfigFactory.systemProperties())(_ withFallback _)
      .withFallback(ConfigFactory.parseResources("application.conf"))
      .withFallback(ConfigFactory.parseResources("reference-overrides.conf"))
      .withFallback(ConfigFactory.parseResources("reference.conf"))
      .resolve()
  }

  def apply(config: TConfig): Config =
    rootConfig(config)
      .map(c => new DefaultConfigImpl(c))
      .get
}
