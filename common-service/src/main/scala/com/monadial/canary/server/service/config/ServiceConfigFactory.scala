package com.monadial.canary.server.service.config

import com.typesafe.config.{Config => TConfig}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ServiceConfigFactory[SC <: ServiceConfig] {

  def make(config: TConfig): SC
}
