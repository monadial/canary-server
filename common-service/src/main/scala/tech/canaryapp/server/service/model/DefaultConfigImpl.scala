package tech.canaryapp.server.service.model

import com.typesafe.config.{ConfigFactory, Config => TConfig}
import tech.canaryapp.server.service.config.{Config, KafkaConfig}

import scala.jdk.CollectionConverters._
import scala.util.Try

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class DefaultConfigImpl(
  rootConfig: TConfig,
  clusterName: ClusterName,
  instanceName: InstanceName,
  serviceName: ServiceName
) extends Config {

  override val logbackXml: String =
    rootConfig.getString("logback.xml")

  override val kafkaConfig: KafkaConfig = new KafkaConfig {
    val kc: TConfig = rootConfig.getConfig("kafka")

    override val bootstrapServers: List[String] =
      kc.getStringList("bootstrap-servers").asScala.toList

    override val topics: List[String] =
      kc.getStringList("topics").asScala.toList

    override val kafkaConsumerSettings: TConfig =
      kc.getConfig("consumer-settings")

    override val kafkaProducerSettings: TConfig =
      kc.getConfig("producer-settings")

    override val kafkaCommitterSettings: TConfig =
     kc.getConfig("committer-settings")
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

  def apply(
    config: TConfig,
    clusterName: ClusterName,
    instanceName: InstanceName,
    serviceName: ServiceName
  ): Config = rootConfig(config)
    .map(c => new DefaultConfigImpl(c, clusterName, instanceName, serviceName))
    .get
}
