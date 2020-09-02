package tech.canaryapp.server.channel

import monix.eval.Task
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.config.Config
import tech.canaryapp.server.service.model.ServiceName

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object ChannelService extends Service {

  override protected val serviceName: ServiceName = ServiceName("channel")

  override protected def start(serviceConfig: Config): Task[Unit] = ???
}
