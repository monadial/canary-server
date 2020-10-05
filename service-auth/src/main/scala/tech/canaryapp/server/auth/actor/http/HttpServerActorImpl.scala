package tech.canaryapp.server.auth.actor.http

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.actor.{ActorSystem => UntypedActorSystem, Scheduler => UntypedScheduler}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import monix.eval.Task
import monix.execution.Scheduler
import tech.canaryapp.server.auth.actor.http.HttpServerActor.{HttpServerBindingAvailable, Message, Provider, Stop}
import tech.canaryapp.server.auth.config.AuthServiceConfig

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object HttpServerActorImpl extends LazyLogging {

  def createActor(config: AuthServiceConfig): Provider =
    () =>
      Behaviors.setup { context =>
        lazy val httpConfig = config.httpService

        // implicits
        implicit val akkaActorSystem: UntypedActorSystem =
          context.system.toClassic

        implicit val akkaScheduler: UntypedScheduler =
          akkaActorSystem.scheduler

        implicit val monixScheduler: Scheduler =
          Scheduler(context.executionContext)


        val bindingTask = Task.fromFuture(
          Http().bindAndHandleAsync(
            Route.asyncHandler(pathSingleSlash {
              get {
                complete(StatusCodes.OK -> "OK")

              }
            }),
            httpConfig.interface,
            httpConfig.port
          )
        )

        context.pipeToSelf(bindingTask.timeout(10 seconds).runToFuture) {
          case Success(value) =>
            logger.info(s"HTTP server bind successful to interface ${httpConfig.interface}:${httpConfig.port}.")
            HttpServerBindingAvailable(value)
          case Failure(exception) =>
            logger.error(s"HTTP server bind unsuccessful to interface ${httpConfig.interface}:${httpConfig.port}.", exception)
            Stop
        }

        def serverInitialization(stopping: Boolean): Behavior[Message] =
          Behaviors.receiveMessagePartial {
            case HttpServerBindingAvailable(binding) =>
              if (stopping) {
                context.self ! Stop
              }
              Behaviors.receiveMessagePartial {
                case Stop =>
                  context.pipeToSelf(binding.terminate(config.gracefulShutdownTimeout)) {
                    case Success(_) =>
                      logger.info("HTTP server shutdown successful")
                      Stop
                    case Failure(exception) =>
                      logger.error("HTTP server shutdown failed", exception)
                      Stop
                  }
                  Behaviors.receiveMessagePartial {
                    case Stop =>
                      Behaviors.stopped
                  }


              }
            case Stop =>
              serverInitialization(true)
          }

        serverInitialization(false)
      }
}
