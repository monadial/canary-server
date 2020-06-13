package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class PublicKey(bytes: Array[Byte]) extends AnyVal with Comparable[PublicKey] {
  override def toString: String = bytes.map(_.toChar).mkString

  override def compareTo(o: PublicKey): Int = BigInt(bytes).compare(BigInt(o.bytes))
}
