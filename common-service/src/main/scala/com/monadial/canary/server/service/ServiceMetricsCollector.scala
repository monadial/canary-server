package com.monadial.canary.server.service

import monix.eval.Task

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ServiceMetricsCollector {
  def start(): Task[Unit]
}
