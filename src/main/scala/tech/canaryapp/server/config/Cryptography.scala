package tech.canaryapp.server.config

import pureconfig.ConfigReader

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class Cryptography(
  certificate: Array[Byte],
  privateKey: Array[Byte],
  expireDays: Int
)

object Cryptography {
  implicit val bytArrayReader: ConfigReader[Array[Byte]] =
    ConfigReader[String].map(_.getBytes)
}
