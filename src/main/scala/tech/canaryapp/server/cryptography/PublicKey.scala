package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
abstract class PublicKey[T](val key: T) extends Ordered[PublicKey[T]] {

  def serialize: T = key

  def asString: String
}
