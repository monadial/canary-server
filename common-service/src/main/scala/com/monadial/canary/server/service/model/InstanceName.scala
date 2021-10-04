package com.monadial.canary.server.service.model

import java.util.UUID

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class InstanceName(
    clusterName: ClusterName,
    serviceName: ServiceName,
    startTime: StartTime
) {
  lazy val instanceUuid: UUID = UUID.randomUUID()

  // mcs = monadial canary server
  override def toString: String = s"${clusterName.name}-mcs-${serviceName.name}-${startTime.asLong}-${instanceUuid.toString}"
}
