package tech.canaryapp.server.cryptography.ellipticCurve

import tech.canaryapp.server.cryptography.{KeyPair, PrivateKey, PublicKey}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait EllipticCurve[PRK <: PrivateKey, PUK <: PublicKey] {

  def generateKeyPair: KeyPair[PRK, PUK]

  def decodePublicPoint(bytes: Array[Byte], offset: Int): PUK

  def decodePrivatePoint(bytes: Array[Byte]): PRK

  def calculateSignature(
    ellipticCurvePrivateKey: PRK,
    message: Array[Byte]
  ): Array[Byte]

  def verifySignature(
    ellipticCurvePublicKey: PUK,
    message: Array[Byte],
    signature: Array[Byte]
  ): Boolean
}
