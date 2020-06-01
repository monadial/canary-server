package tech.canaryapp.server.cryptography.ellipticCurve.ec25519

import tech.canaryapp.server.cryptography.KeyPair
import tech.canaryapp.server.cryptography.ellipticCurve.EllipticCurve

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final class Ec25519EllipticCurve extends EllipticCurve[Ec25519PrivateKey, Ec25519PublicKey] {

  override def generateKeyPair: KeyPair[Ec25519PrivateKey, Ec25519PublicKey] = {
    val keyPair = curve25519.generateKeyPair();

    KeyPair(Ec25519PrivateKey(keyPair.getPrivateKey), Ec25519PublicKey(keyPair.getPublicKey));
  }

  override def decodePublicPoint(bytes: Array[Byte], offset: Int): Ec25519PublicKey = ???

  override def decodePrivatePoint(bytes: Array[Byte]): Ec25519PrivateKey = ???

  override def calculateSignature(ellipticCurvePrivateKey: Ec25519PrivateKey, message: Array[Byte]): Array[Byte] = ???

  override def verifySignature(ellipticCurvePublicKey: Ec25519PublicKey, message: Array[Byte], signature: Array[Byte]): Boolean = ???
}
