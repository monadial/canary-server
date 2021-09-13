package tech.canaryapp.server.util.reusable.actor.http

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import monix.execution.Scheduler
import tech.canaryapp.server.util.reusable.actor.http.HttpServerActor.{HttpServerBindingAvailable, Message, ServerProvider, Stop}
import tech.canaryapp.server.util.reusable.actor.http.config.HttpServerConfig
import tech.canaryapp.server.util.reusable.actor.http.service.HttpService
import akka.actor.{ActorSystem => UntypedActorSystem}
import akka.actor.{Scheduler => UntypedScheduler}
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import monix.eval.Task

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}


object HttpServerActorImpl extends LazyLogging {

  private type HttpServer[CFG <: HttpServerConfig, HS <: HttpService[CFG]] = ServerProvider[CFG, HS]

  def createActor[CFG <: HttpServerConfig, HS <: HttpService[CFG]](config: CFG, service: HS): HttpServer[CFG, HS] =
    () =>
      Behaviors.setup { context =>
        // implicits
        implicit val akkaActorSystem: UntypedActorSystem =
          context.system.toClassic

        implicit val akkaScheduler: UntypedScheduler =
          akkaActorSystem.scheduler

        implicit val monixScheduler: Scheduler =
          Scheduler(context.executionContext)

        val bindingTask = Task.fromFuture(
          Http()
            .newServerAt(config.interface, config.port)
            .bind(service.routes)
        );

        context.pipeToSelf(bindingTask.timeout(10 seconds).runToFuture) {
          case Success(value) =>
            logger.info(s"HTTP server bind successful to interface ${config.interface}:${config.port}.")
            HttpServerBindingAvailable(value)
          case Failure(exception) =>
            logger.error(
              s"HTTP server bind unsuccessful to interface ${config.interface}:${config.port}.",
              exception
            )
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
