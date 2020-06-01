package tech.canaryapp.server.cryptography.ellipticCurve.ec25519

import tech.canaryapp.server.cryptography.PrivateKey

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class Ec25519PrivateKey(privateKey: Array[Byte]) extends PrivateKey {

  override def serialize: Array[Byte] = privateKey;
}
