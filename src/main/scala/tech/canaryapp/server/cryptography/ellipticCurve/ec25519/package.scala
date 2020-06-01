package tech.canaryapp.server.cryptography.ellipticCurve

import org.whispersystems.curve25519.Curve25519

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
package object ec25519 {

  // default curve instance
  def curve25519: Curve25519 = Curve25519.getInstance(Curve25519.BEST)
}
