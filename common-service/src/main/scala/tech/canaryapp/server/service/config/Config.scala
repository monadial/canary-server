package tech.canaryapp.server.service.config

import com.typesafe.config.{Config => TConfig}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait Config {

  val rootConfig: TConfig

  val kafkaConfig: KafkaConfig

  val logbackXml: String
}
