package tech.canaryapp.server.cryptography.ellipticCurve

import tech.canaryapp.server.cryptography.{KeyPair, PrivateKey, PublicKey}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait EllipticCurve[PRK <: PrivateKey[_], PUK <: PublicKey[_]] {

  def generateKeyPair: KeyPair[PRK, PUK]

  def decodePublicPoint(bytes: Array[Byte], offset: Int): Either[Throwable, PUK]

  def decodePrivatePoint(bytes: Array[Byte]): PRK

  def calculateArgument(privateKey: PRK, publicKey: PUK): Array[Byte]

  def calculateSignature(
    publicKey: PRK,
    message: Array[Byte]
  ): Array[Byte]

  def verifySignature(
    publicKey: PUK,
    message: Array[Byte],
    signature: Array[Byte]
  ): Boolean
}
