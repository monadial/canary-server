package com.monadial.canary.server.nonce

import akka.actor.CoordinatedShutdown
import akka.actor.typed.scaladsl.AskPattern._
import monix.eval.Task
import akka.actor.typed.scaladsl.adapter._
import akka.util.Timeout
import akka.actor.typed.ActorSystem
import akka.actor.typed.{Scheduler => AkkaScheduler}
import com.monadial.canary.server.nonce.actor.ActorModule
import com.monadial.canary.server.nonce.actor.supervisor.SupervisorActor
import com.monadial.canary.server.nonce.actor.supervisor.SupervisorActor.Stop
import com.monadial.canary.server.nonce.config.{NonceServiceConfig, NonceServiceConfigImpl}
import com.monadial.canary.server.service.Service
import com.monadial.canary.server.service.config.Config
import com.monadial.canary.server.service.model.{InstanceName, ServiceName}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutor

object NonceService extends Service {

  override protected val serviceName: ServiceName = ServiceName("nonce")

  override protected def start(serviceConfig: Config): Task[Unit] = {
    val nonceServiceConfig = NonceServiceConfigImpl(serviceConfig)
    Task
      .eval[ActorModule](new ActorModule {
        override lazy val config: NonceServiceConfig = nonceServiceConfig
      })
      .bracket { module =>
        Task.cancelable[Unit] { callback =>
          lazy val actorSystem =
            ActorSystem(module.supervisorActorProvider(), clusterName.name, module.config.systemConfig.rootConfig)

          CoordinatedShutdown(actorSystem.toClassic)
            .addTask(CoordinatedShutdown.PhaseBeforeActorSystemTerminate, "stopActors") { () =>
              implicit val scheduler: AkkaScheduler = actorSystem.scheduler
              implicit val timeout: Timeout = Timeout(module.config.gracefulShutdownTimeout)
              actorSystem ? Stop
            }

          implicit val ec: ExecutionContextExecutor = ExecutionContext.global

          actorSystem.whenTerminated
            .map(_ => ())
            .onComplete(callback.apply)

          Task.eval(
            CoordinatedShutdown(actorSystem.toClassic)
              .run(CoordinatedShutdown.UnknownReason))
        }
      }(_ => Task.unit)

  }
}
