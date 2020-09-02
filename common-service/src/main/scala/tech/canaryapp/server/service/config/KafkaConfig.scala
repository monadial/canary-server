package tech.canaryapp.server.service.config

import com.typesafe.config.{Config => TConfig}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait KafkaConfig {

  val bootstrapServers: List[String]

  val messagesTopicName: String

  val kafkaConsumerSettings: TConfig

  val kafkaProducerSettings: TConfig
}
