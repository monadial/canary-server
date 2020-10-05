package tech.canaryapp.server.service.config

import com.typesafe.config.{Config => TConfig}
import tech.canaryapp.server.service.model.{ClusterName, InstanceName, ServiceName}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait Config {

  val rootConfig: TConfig

  val clusterName: ClusterName

  val instanceName: InstanceName

  val serviceName: ServiceName

  val kafkaConfig: KafkaConfig

  val logbackXml: String
}
