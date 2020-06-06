package tech.canaryapp.server.actor.server

import akka.actor
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import doobie.hikari.HikariTransactor
import doobie.implicits._
import monix.eval.Task
import monix.execution.Scheduler
import tech.canaryapp.server.actor.server.ServerActor.{Message, ServerBindingAvailable, Stop}
import tech.canaryapp.server.config.CanaryConfig

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object ServerActorImpl {

  def createActor(config: CanaryConfig, tx: HikariTransactor[Task]): ServerActor.Provider =
    () =>
      Behaviors.setup { context =>
        // implicits
        implicit val akkaActorSystem: actor.ActorSystem =
          context.system.toClassic

        implicit val akkaScheduler: actor.Scheduler =
          akkaActorSystem.scheduler

        implicit val monixScheduler: Scheduler =
          monix.execution.Scheduler(context.executionContext)

        lazy val interface = config.httpServer.interface
        lazy val port = config.httpServer.port

        val serverBindingTask: Task[Http.ServerBinding] = Task.fromFuture {
          Http(akkaActorSystem).bindAndHandle(get {

            val task = sql"""SELECT version();""".query[String].analysis.transact(tx)

            onComplete(task.runToFuture) {
              case Success(value) => complete(value.toString)
              case Failure(ex)    => complete(s"An error occurred: ${ex.getMessage}")
            }
          }, interface, port)
        }

        context.pipeToSelf(serverBindingTask.timeout(10 seconds).runToFuture) {
          case Success(binding) =>
            context.log.info(s"""Canary Server bind successful: $interface:$port.""")
            ServerBindingAvailable(binding)
          case Failure(error) =>
            context.log.error("Canary server start failed.", error)
            Stop
        }

        def handleShutdownBehavior(binding: Http.ServerBinding): Behavior[Message] =
          Behaviors.receiveMessagePartial {
            case Stop =>
              context.pipeToSelf(binding.terminate(config.gracefulShutdownTimeout)) {
                case Success(_) =>
                  context.log.info("Canary HTTP server shutdown successful.")
                  Stop
                case Failure(error) =>
                  context.log.error("Canary HTTP server shutdown failed.", error)
                  Stop
              }

              Behaviors.receiveMessagePartial {
                case Stop =>
                  Behaviors.stopped
              }
          }

        def runBehavior(): Behavior[Message] =
          Behaviors.receiveMessagePartial {
            case ServerBindingAvailable(binding) =>
              handleShutdownBehavior(binding)
            case Stop =>
              context.log.info("Stop4")
              runBehavior()
          }

        runBehavior()
      }
}
