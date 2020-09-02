package tech.canaryapp.server.email

import monix.eval.Task
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.config.Config
import tech.canaryapp.server.service.model.ServiceName

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object EmailService extends Service {

  override protected val serviceName: ServiceName = ServiceName("email")

  override protected def start(serviceConfig: Config): Task[Unit] = ???
}
