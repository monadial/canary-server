package tech.canarypp.server.notification

import monix.eval.Task
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.config.Config
import tech.canaryapp.server.service.model.ServiceName

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object NotificationService extends Service {

  override protected val serviceName: ServiceName = ServiceName("notification")

  override protected def start(serviceConfig: Config): Task[Unit] = ???
}
