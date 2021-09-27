package com.monadial.canary.server.auth.config

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait HttpServerConfig {
  val interface: String
  val port: Int

  override def toString: String = interface + ":" + port
}
