package tech.canaryapp.server.cryptography

import org.whispersystems.curve25519.Curve25519
import tech.canaryapp.server.cryptography.ellipticCurve.EllipticCurve
import tech.canaryapp.server.cryptography.ellipticCurve.ec25519.Ec25519EllipticCurve

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait CryptographyModule {

  lazy val ellipticCurve: Ec25519EllipticCurve =
    Ec25519EllipticCurve.apply(Curve25519.getInstance(Curve25519.BEST))
}
