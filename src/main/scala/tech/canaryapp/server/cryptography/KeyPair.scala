package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class KeyPair[PRK <: PrivateKey[_], PUK <: PublicKey[_]](
  privateKey: PRK, publicKey: PUK
)
