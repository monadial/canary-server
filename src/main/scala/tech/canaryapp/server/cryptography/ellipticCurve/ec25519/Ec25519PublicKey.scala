package tech.canaryapp.server.cryptography.ellipticCurve.ec25519

import java.util.Base64

import tech.canaryapp.server.cryptography.PublicKey

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final case class Ec25519PublicKey(private val ec25519PublicKey: Array[Byte]) extends PublicKey(ec25519PublicKey) {

  override def compare(that: PublicKey[Array[Byte]]): Int =
    BigInt(ec25519PublicKey).compare(BigInt(that.key))

  override def asString: String = Base64.getEncoder.encodeToString(ec25519PublicKey)

  override def toString: String = asString
}
