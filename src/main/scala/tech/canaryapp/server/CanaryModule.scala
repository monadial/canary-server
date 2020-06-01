package tech.canaryapp.server

import tech.canaryapp.server.actor.ActorModule
import tech.canaryapp.server.cryptography.CryptographyModule

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait CanaryModule extends CryptographyModule
  with ActorModule {

}
