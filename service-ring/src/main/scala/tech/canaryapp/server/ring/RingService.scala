package tech.canaryapp.server.ring

import monix.eval.Task
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.config.Config
import tech.canaryapp.server.service.model.ServiceName

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object RingService extends Service {

  override protected val serviceName:
    ServiceName = ServiceName("ring")

  override protected def start(serviceConfig: Config): Task[Unit] =
    Task.now(Task.unit)
}
