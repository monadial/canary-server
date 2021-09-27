package com.monadial.canary.server.service.model

import com.monadial.canary.server.service.ReadFromEnvironment

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class ClusterName(name: String) extends AnyVal

object ClusterName {

  val readFromEnvironment: ReadFromEnvironment[ClusterName] =
    () => sys.env.get("CLUSTER_NAME").map(ClusterName(_))
}
