package com.monadial.canary.server.auth

import akka.actor.CoordinatedShutdown
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.{ActorSystem, Scheduler => AkkaScheduler}
import akka.util.Timeout
import com.monadial.canary.server.auth.actor.ActorModule
import com.monadial.canary.server.auth.actor.supervisor.SupervisorActor
import com.monadial.canary.server.auth.config.{AuthServiceConfig, AuthServiceConfigImpl}
import com.monadial.canary.server.auth.persistence.DoobieHikariTransactor
import monix.eval.Task
import monix.execution
import monix.execution.Scheduler
import org.apache.kafka.clients.producer.Producer
import com.monadial.canary.server.auth.config.AuthServiceConfigImpl
import com.monadial.canary.server.auth.persistence.FlywayDoobieMigration
import com.monadial.canary.server.auth.persistence.DoobieHikariTransactor.Transactor
import com.monadial.canary.server.service.Service
import com.monadial.canary.server.service.config.Config
import com.monadial.canary.server.service.model.ServiceName
import com.monadial.canary.server.service.Service

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.language.postfixOps

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object AuthService extends Service {

  override protected val serviceName:
    ServiceName = ServiceName("auth")

  override protected def start(serviceConfig: Config): Task[Unit] = {
    val authServiceConfig = AuthServiceConfigImpl(serviceConfig)
    DoobieHikariTransactor.instance(authServiceConfig.database).use { tx =>
      Task.eval[ActorModule](new ActorModule {
        override val config: AuthServiceConfig = authServiceConfig

        override val transactor: Transactor = tx

        override val scheduler: Scheduler =
          Scheduler.computation(config.parallelism, "canary-auth-message-consumer")

        override val kafkaProducer: Producer[Array[Byte], Array[Byte]] = config
          .kafkaProducerSettings
          .createKafkaProducer()
      })
        .bracket { module =>
          Task.cancelable[Unit] { callback =>
            val actorSystem = ActorSystem(
              module.supervisorActorProvider(),
              clusterName.name,
              module.config.systemConfig.rootConfig
            )

            CoordinatedShutdown(actorSystem.toClassic)
              .addTask(CoordinatedShutdown.PhaseBeforeActorSystemTerminate, "stopActors") { () =>
                implicit val scheduler: AkkaScheduler = actorSystem.scheduler
                implicit val timeout: Timeout = Timeout(module.config.gracefulShutdownTimeout)
                actorSystem ? SupervisorActor.Stop
              }

            implicit val ec: ExecutionContextExecutor = ExecutionContext.global

            actorSystem
              .whenTerminated
              .map(_ => ())
              .onComplete(callback.apply)

            Task.eval(CoordinatedShutdown(actorSystem.toClassic)
              .run(CoordinatedShutdown.UnknownReason))
          }
        }(_ => Task.unit)
    }
  }
}
