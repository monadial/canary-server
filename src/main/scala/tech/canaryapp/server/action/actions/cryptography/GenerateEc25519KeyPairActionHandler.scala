package tech.canaryapp.server.action.actions.cryptography

import monix.eval.Task
import tech.canaryapp.server.action.ActionHandler
import tech.canaryapp.server.cryptography.KeyPair
import tech.canaryapp.server.cryptography.ellipticCurve.EllipticCurve

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final class GenerateEc25519KeyPairActionHandler(ellipticCurve: EllipticCurve)
    extends ActionHandler[GenerateEc25519KeyPairAction] {

  override def handle(action: GenerateEc25519KeyPairAction): Task[KeyPair] =
    Task
      .eval(ellipticCurve.generateKeyPair)
      .memoize
}
