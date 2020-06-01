package tech.canaryapp

import tech.canaryapp.actor.ActorModule
import tech.canaryapp.cryptography.CryptographyModule

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait CanaryModule extends CryptographyModule
  with ActorModule {

}
