package tech.canaryapp.server

import tech.canaryapp.server.actor.ActorModule
import tech.canaryapp.server.config.CanaryConfig
import tech.canaryapp.server.cryptography.CryptographyModule
import tech.canaryapp.server.database.DatabaseModule

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
trait CanaryModule extends CryptographyModule with ActorModule with DatabaseModule {

  val configuration: CanaryConfig
}
