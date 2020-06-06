package tech.canaryapp.server.cryptography.ellipticCurve.ec25519

import java.security.InvalidKeyException

import org.whispersystems.curve25519.Curve25519
import tech.canaryapp.server.cryptography.KeyPair
import tech.canaryapp.server.cryptography.ellipticCurve.EllipticCurve

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final class Ec25519EllipticCurve(curve: Curve25519) extends EllipticCurve[Array[Byte], Ec25519PrivateKey, Ec25519PublicKey] {

  override def generateKeyPair: KeyPair[Ec25519PrivateKey, Ec25519PublicKey] = {
    val keyPair = curve.generateKeyPair();

    KeyPair(Ec25519PrivateKey(keyPair.getPrivateKey), Ec25519PublicKey(keyPair.getPublicKey))
  }

  override def decodePublicPoint(bytes: Array[Byte], offset: Int): Either[Throwable, Ec25519PublicKey] = {

    if (bytes.length - offset < 1) Left(new InvalidKeyException("Invalid key offset"))

    val keyBytes = new Array[Byte](32)
    System.arraycopy(bytes, offset + 1, keyBytes, 0, keyBytes.length)

    Right(Ec25519PublicKey(keyBytes))
  }

  override def calculateAgreement(privateKey: Ec25519PrivateKey, publicKey: Ec25519PublicKey): Array[Byte] =
    curve.calculateAgreement(publicKey.key, privateKey.key)

  override def decodePrivatePoint(bytes: Array[Byte]): Ec25519PrivateKey =
    Ec25519PrivateKey(bytes)

  override def calculateSignature(publicKey: Ec25519PrivateKey, message: Array[Byte]): Array[Byte] =
    curve.calculateSignature(publicKey.key, message)

  override def verifySignature(
      publicKey: Ec25519PublicKey,
      message: Array[Byte],
      signature: Array[Byte]
  ): Boolean =
    curve.verifySignature(publicKey.key, message, signature)
}
object Ec25519EllipticCurve {

  def apply(curve25519: Curve25519): Ec25519EllipticCurve =
    new Ec25519EllipticCurve(curve25519)
}
