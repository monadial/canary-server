package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait PublicKey extends Ordered[PublicKey]{
  def serialize: Array[Byte]
}
