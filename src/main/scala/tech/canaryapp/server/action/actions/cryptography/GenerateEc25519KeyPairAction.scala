package tech.canaryapp.server.action.actions.cryptography

import tech.canaryapp.server.action.Action
import tech.canaryapp.server.cryptography.KeyPair

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
case class GenerateEc25519KeyPairAction() extends Action {
  override type Out = KeyPair
}
