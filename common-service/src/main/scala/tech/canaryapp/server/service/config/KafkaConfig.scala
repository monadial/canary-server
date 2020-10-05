package tech.canaryapp.server.service.config

import com.typesafe.config.{Config => TConfig}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait KafkaConfig {

  val bootstrapServers: List[String]

  val topics: List[String]

  val kafkaConsumerSettings: TConfig

  val kafkaProducerSettings: TConfig

  val kafkaCommitterSettings: TConfig
}
