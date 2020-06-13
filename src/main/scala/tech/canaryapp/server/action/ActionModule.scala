package tech.canaryapp.server.action

import shapeless.HNil
import tech.canaryapp.server.action.actions.cryptography.GenerateEc25519KeyPairAction

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ActionModule {

  protected  val generateEc25519KeyPairActionHandler: ActionHandler[GenerateEc25519KeyPairAction]

  val actionDispatcher = ActionDispatcher.of(
    generateEc25519KeyPairActionHandler
    :: HNil
  )
}
