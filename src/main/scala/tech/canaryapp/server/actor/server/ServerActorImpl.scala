package tech.canaryapp.server.actor.server

import akka.actor
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import monix.eval.Task
import monix.execution.Scheduler
import tech.canaryapp.server.actor.server.ServerActor.Message
import tech.canaryapp.server.actor.server.ServerActor.ServerBindingAvailable
import tech.canaryapp.server.actor.server.ServerActor.Stop
import tech.canaryapp.server.actor.server.http.Endpoint
import tech.canaryapp.server.config.CanaryConfig

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Failure
import scala.util.Success

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object ServerActorImpl {

  def createActor(config: CanaryConfig, endpoints: List[Endpoint]): ServerActor.Provider =
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
          val routes = endpoints
            .map(_.routes)
            .reduce(_ ~ _)

          Http(akkaActorSystem).bindAndHandle(routes, interface, port)
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
