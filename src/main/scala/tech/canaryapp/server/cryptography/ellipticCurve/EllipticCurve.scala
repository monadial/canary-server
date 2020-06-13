package tech.canaryapp.server.cryptography.ellipticCurve

import tech.canaryapp.server.cryptography.{KeyPair, PrivateKey, PublicKey, SharedSecret}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait EllipticCurve {

  def generateKeyPair: KeyPair

  def decodePublicPoint(bytes: Array[Byte], offset: Int): Either[Throwable, PublicKey]

  def decodePrivatePoint(bytes: Array[Byte]): PrivateKey

  def calculateAgreement(privateKey: PublicKey, publicKey: PrivateKey): SharedSecret

  def calculateSignature(
    publicKey: PrivateKey,
    message: Array[Byte]
  ): Array[Byte]

  def verifySignature(
    publicKey: PublicKey,
    message: Array[Byte],
    signature: Array[Byte]
  ): Boolean
}
