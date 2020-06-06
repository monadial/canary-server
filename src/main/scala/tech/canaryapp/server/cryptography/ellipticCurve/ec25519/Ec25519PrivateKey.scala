package tech.canaryapp.server.cryptography.ellipticCurve.ec25519

import java.util.Base64

import tech.canaryapp.server.cryptography.PrivateKey

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final case class Ec25519PrivateKey(private val ec25519PrivateKey: Array[Byte])
  extends PrivateKey(ec25519PrivateKey) {

  override def asString: String = Base64.getEncoder.encodeToString(ec25519PrivateKey)

  override def toString: String = asString
}
