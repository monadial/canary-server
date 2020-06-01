package tech.canaryapp.server.cryptography.ellipticCurve.ec25519

import tech.canaryapp.server.cryptography.PublicKey

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
case class Ec25519PublicKey(publicKey: Array[Byte]) extends PublicKey {

  override def serialize: Array[Byte] = publicKey

  override def compare(that: PublicKey): Int = this.compare(that)
}
