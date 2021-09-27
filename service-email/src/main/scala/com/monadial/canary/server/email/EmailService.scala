package com.monadial.canary.server.email

import com.monadial.canary.server.service.Service
import com.monadial.canary.server.service.config.Config
import com.monadial.canary.server.service.model.ServiceName
import monix.eval.Task

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object EmailService extends Service {

  override protected val serviceName: ServiceName = ServiceName("email")

  override protected def start(serviceConfig: Config): Task[Unit] =
    Task.now(Task.unit)
}
