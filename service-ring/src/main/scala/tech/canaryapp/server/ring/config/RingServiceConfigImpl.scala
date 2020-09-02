package tech.canaryapp.server.ring.config

import com.typesafe.config.{Config => TConfig}
import tech.canaryapp.server.service.config.ServiceConfigFactory

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class RingServiceConfigImpl(rootConfig: TConfig) extends RingServiceConfig

object RingServiceConfigImpl {

  val factory: ServiceConfigFactory[RingServiceConfig] =
    (config: TConfig) =>
      RingServiceConfigImpl(config)

}
