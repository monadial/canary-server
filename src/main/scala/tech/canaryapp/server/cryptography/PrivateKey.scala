package tech.canaryapp.server.cryptography

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
case class PrivateKey(bytes: Array[Byte]) extends AnyVal with Comparable[PrivateKey] {
  override def toString: String = bytes.map(_.toChar).mkString

  override def compareTo(o: PrivateKey): Int = BigInt(bytes).compare(BigInt(o.bytes))
}
