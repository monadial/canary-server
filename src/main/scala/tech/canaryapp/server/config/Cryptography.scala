package tech.canaryapp.server.config

import scala.concurrent.duration.FiniteDuration
import scala.language.postfixOps

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final case class Cryptography(certificate: String, privateKey: String, expireDays: FiniteDuration) {
  lazy val certificateAsArrayByte: Array[Byte] = stringToArrayByte(certificate)
  lazy val privateKeyAsArrayByte: Array[Byte] = stringToArrayByte(privateKey)
  lazy val expireAsInt: Long = expireDays.toDays

  private def stringToArrayByte: String => Array[Byte] = (string: String) => string.getBytes
}
