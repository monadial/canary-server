package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
abstract class PrivateKey[T](val key: T) {

  def serialize: T = key

  def asString: String
}
