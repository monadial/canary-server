package tech.canaryapp.server.ring

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.adapter._
import akka.actor.{Actor, CoordinatedShutdown}
import akka.actor.typed.ActorSystem
import monix.eval.Task
import tech.canaryapp.server.ring.actor.ActorModule
import tech.canaryapp.server.ring.config.{RingServiceConfig, RingServiceConfigImpl}
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.config.{Config, ServiceConfigFactory}
import tech.canaryapp.server.service.model.ServiceName

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object RingService extends Service[RingServiceConfig] {

  override protected val serviceName:
    ServiceName = ServiceName("ring")

  override protected implicit val configFactory:
    ServiceConfigFactory[RingServiceConfig] = RingServiceConfigImpl.factory

  override protected def start(sc: Config[RingServiceConfig]): Task[Unit] =
    Task.eval[ActorModule](new ActorModule {
      override val moduleConfig: Config[RingServiceConfig] = sc
    }).bracket(module => {
      Task.cancelable[Unit] { callback =>
        val serviceConfig = module.moduleConfig.serviceConfig
        val actorSystem = ActorSystem(
            module.supervisorActor(),
            clusterName.name,
            module.moduleConfig.rootConfig
          )

        CoordinatedShutdown(actorSystem.toClassic)

      }
    })
}
