package com.monadial.canary.server.channel

import com.monadial.canary.server.service.Service
import com.monadial.canary.server.service.config.Config
import com.monadial.canary.server.service.model.ServiceName
import monix.eval.Task

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object ChannelService extends Service {

  override protected val serviceName: ServiceName = ServiceName("channel")

  override protected def start(serviceConfig: Config): Task[Unit] =
    Task.now(Task.unit)
}
