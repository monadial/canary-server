package tech.canaryapp.server.auth

import akka.actor.CoordinatedShutdown
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.{ActorSystem, Scheduler}
import akka.util.Timeout
import monix.eval.Task
import org.apache.kafka.clients.producer.Producer
import tech.canaryapp.server.auth.actor.ActorModule
import tech.canaryapp.server.auth.actor.supervisor.SupervisorActor
import tech.canaryapp.server.auth.config.{AuthServiceConfig, AuthServiceConfigImpl}
import tech.canaryapp.server.auth.persistence.DoobieHikariTransactor
import tech.canaryapp.server.auth.persistence.DoobieHikariTransactor.Transactor
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.config.Config
import tech.canaryapp.server.service.model.ServiceName

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

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
                implicit val scheduler: Scheduler = actorSystem.scheduler
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
