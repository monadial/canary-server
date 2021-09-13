package tech.canaryapp.server.nonce

import akka.actor.CoordinatedShutdown
import akka.actor.typed.scaladsl.AskPattern._
import monix.eval.Task
import akka.actor.typed.scaladsl.adapter._
import akka.util.Timeout
import akka.actor.typed.ActorSystem
import akka.actor.typed.{Scheduler => AkkaScheduler}
import tech.canaryapp.server.nonce.actor.ActorModule
import tech.canaryapp.server.nonce.actor.supervisor.SupervisorActor
import tech.canaryapp.server.nonce.config.NonceServiceConfig
import tech.canaryapp.server.nonce.config.NonceServiceConfigImpl
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.config.Config
import tech.canaryapp.server.service.model.ServiceName

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutor

object NonceService extends Service {

  override protected val serviceName: ServiceName = ServiceName("nonce")

  override protected def start(serviceConfig: Config): Task[Unit] = {
    val nonceServiceConfig = NonceServiceConfigImpl(serviceConfig)
    Task
      .eval[ActorModule](new ActorModule {
        override val config: NonceServiceConfig = nonceServiceConfig
      })
      .bracket { module =>
        Task.cancelable[Unit] { callaback =>
          lazy val actorSystem =
            ActorSystem(module.supervisorActorProvider(), clusterName.name, module.config.systemConfig.rootConfig)

          CoordinatedShutdown(actorSystem.toClassic)
            .addTask(CoordinatedShutdown.PhaseBeforeActorSystemTerminate, "stopActors") { () =>
              implicit val scheduler: AkkaScheduler = actorSystem.scheduler
              implicit val timeout: Timeout = Timeout(module.config.gracefulShutdownTimeout)
              actorSystem ? SupervisorActor.Stop
            }

          implicit val ec: ExecutionContextExecutor = ExecutionContext.global

          actorSystem.whenTerminated
            .map(_ => ())
            .onComplete(callaback.apply)

          Task.eval(
            CoordinatedShutdown(actorSystem.toClassic)
              .run(CoordinatedShutdown.UnknownReason))
        }
      }(_ => Task.unit)

  }
}
