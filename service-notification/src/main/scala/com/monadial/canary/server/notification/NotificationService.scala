package com.monadial.canary.server.notification

import com.monadial.canary.server.service.Service
import com.monadial.canary.server.service.config.Config
import com.monadial.canary.server.service.model.ServiceName
import monix.eval.Task

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object NotificationService extends Service {

  override protected val serviceName: ServiceName = ServiceName("notification")

  override protected def start(serviceConfig: Config): Task[Unit] =
    Task.now(Task.unit)
}
