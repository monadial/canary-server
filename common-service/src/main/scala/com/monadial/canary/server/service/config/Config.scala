package com.monadial.canary.server.service.config

import com.monadial.canary.server.service.model.{ClusterName, InstanceName, ServiceName}
import com.typesafe.config.{Config => TConfig}
import com.monadial.canary.server.service.model.ServiceName

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
