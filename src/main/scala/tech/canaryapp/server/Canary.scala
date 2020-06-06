package tech.canaryapp.server

import akka.actor.{CoordinatedShutdown, typed}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.adapter._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import doobie.hikari.HikariTransactor
import monix.eval.Task
import monix.execution.Scheduler
import pureconfig._
import tech.canaryapp.server.actor.supervisor.SupervisorActor
import tech.canaryapp.server.config.CanaryConfig
import tech.canaryapp.server.transactor.DoobieHikariTransactor

import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object Canary extends LazyLogging {

  private lazy val rootConfig = ConfigFactory.load()

  def main(args: Array[String]): Unit = {
    val cancelableFuture = Task
      .fromTry {
        Try {
          import pureconfig.generic.auto._
          ConfigSource
            .fromConfig(rootConfig.getConfig("canary"))
            .loadOrThrow[CanaryConfig]
        }
      }
      .flatMap { config =>
        lazy val transactor = DoobieHikariTransactor.instance(config.database)

        transactor.use { tx =>
          Task
            .eval[CanaryModule](new CanaryModule {
              override val configuration: CanaryConfig = config
              override val transactor: HikariTransactor[Task] = tx
            })
            .bracket { module =>
              Task.cancelable[Unit] { callback =>
                val system = ActorSystem(module.supervisorActor(), "canary", rootConfig)

                CoordinatedShutdown(system.toClassic)
                  .addTask(CoordinatedShutdown.PhaseBeforeActorSystemTerminate, "stopActors") { () =>
                    implicit val scheduler: typed.Scheduler = system.scheduler
                    implicit val timeout: Timeout = Timeout(config.gracefulShutdownTimeout)
                    system ? SupervisorActor.Stop
                  }

                implicit val ec: ExecutionContextExecutor =
                  ExecutionContext.global

                system.whenTerminated
                  .map(_ => ())
                  .onComplete(callback.apply)

                Task.eval(CoordinatedShutdown(system.toClassic)
                  .run(CoordinatedShutdown.UnknownReason))
              }
            }(_ => Task.unit)
        }
      }
      .runToFuture(Scheduler.global)

    Try(Await.result(cancelableFuture, Duration.Inf)) match {
      case Failure(error) =>
        error.printStackTrace(System.err)
        logger.error("Canary Server run failed.", error)
        System.exit(1)
      case Success(_) =>
        logger.info("Canary server run completed!")
        System.exit(0)
    }

  }

}
