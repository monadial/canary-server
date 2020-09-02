package tech.canaryapp.server.service.config

import scala.concurrent.duration.Duration

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ServiceConfig {
  val systemConfig: Config

  Duration
}
