package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait PrivateKey {
  def serialize: Array[Byte]
}
