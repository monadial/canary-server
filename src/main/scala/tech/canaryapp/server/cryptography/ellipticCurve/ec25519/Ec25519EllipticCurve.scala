package tech.canaryapp.server.cryptography.ellipticCurve.ec25519

import java.security.InvalidKeyException

import org.whispersystems.curve25519.Curve25519
import tech.canaryapp.server.cryptography.{KeyPair, PrivateKey, PublicKey, SharedSecret}
import tech.canaryapp.server.cryptography.ellipticCurve.EllipticCurve

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final class Ec25519EllipticCurve(curve: Curve25519) extends EllipticCurve {

  override def generateKeyPair: KeyPair = {
    val keyPair = curve.generateKeyPair()

    KeyPair(PublicKey(keyPair.getPrivateKey), PrivateKey(keyPair.getPublicKey))
  }

  override def decodePublicPoint(bytes: Array[Byte], offset: Int): Either[Throwable, PublicKey] = {

    if (bytes.length - offset < 1) Left(new InvalidKeyException("Invalid key offset"))

    val keyBytes = new Array[Byte](32)
    System.arraycopy(bytes, offset + 1, keyBytes, 0, keyBytes.length)

    Right(PublicKey(keyBytes))
  }

  override def calculateAgreement(publicKey: PublicKey, privateKey: PrivateKey): SharedSecret = {
    val agreement = curve.calculateAgreement(publicKey.bytes, privateKey.bytes)

    SharedSecret(agreement)
  }

  override def decodePrivatePoint(bytes: Array[Byte]): PrivateKey =
    PrivateKey(bytes)

  override def calculateSignature(publicKey: PrivateKey, message: Array[Byte]): Array[Byte] =
    curve.calculateSignature(publicKey.bytes, message)

  override def verifySignature(
      publicKey: PublicKey,
      message: Array[Byte],
      signature: Array[Byte]
  ): Boolean =
    curve.verifySignature(publicKey.bytes, message, signature)
}
object Ec25519EllipticCurve {

  def apply(curve25519: Curve25519): Ec25519EllipticCurve =
    new Ec25519EllipticCurve(curve25519)
}
