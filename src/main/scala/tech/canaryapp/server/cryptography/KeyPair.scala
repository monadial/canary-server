package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class KeyPair[PRK <: PrivateKey, PUK <: PublicKey](
  privateKey: PRK, publicKey: PUK
)
