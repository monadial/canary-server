package tech.canaryapp.server.implicits

import java.util.Base64

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object StringOps {

  implicit class StringToBase64(x: String) {
    def toBase64: String = Base64.getEncoder.encodeToString(x.getBytes)

    def fromBase64(): Array[Byte] = Base64.getDecoder.decode(x)
  }

}
