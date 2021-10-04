package com.monadial.canary.server.nonce.actor.http

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.actor.{ActorSystem => UntypedActorSystem}
import akka.actor.{Scheduler => UntypedScheduler}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.monadial.canary.server.nonce.actor.http.HttpServerActor.HttpServerBindingAvailable
import com.monadial.canary.server.nonce.actor.http.HttpServerActor.Provider
import com.monadial.canary.server.nonce.actor.http.HttpServerActor.Stop
import com.monadial.canary.server.nonce.actor.http.HttpServerActor.Message
import com.monadial.canary.server.nonce.actor.http.resource.Resource
import com.monadial.canary.server.nonce.actor.http.resource.v1.HealthCheckResource
import com.monadial.canary.server.nonce.actor.http.resource.v1.HomeResource
import com.monadial.canary.server.nonce.actor.http.resource.v1.NewNonceResource
import com.monadial.canary.server.nonce.config.NonceHttpServiceConfig
import com.monadial.canary.server.nonce.config.NonceServiceConfig
import com.monadial.canary.server.service.model.InstanceName
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Failure
import scala.util.Success

object HttpServerActorImpl {

  def createActor(serviceConfig: NonceServiceConfig): Provider =
    (nonceActor) =>
      Behaviors.setup { context =>
        val httpConfig = serviceConfig.httpServiceConfig

        implicit val akkaActorSystem: UntypedActorSystem =
          context.system.toClassic

        implicit val monixScheduler: Scheduler =
          Scheduler(context.executionContext)

        lazy val routes: Route =
          pathSingleSlash {
            HomeResource().routes
          } ~ pathPrefix("v1") {
            pathPrefix("new-nonce")(NewNonceResource(nonceActor).routes) ~
              pathPrefix("new-nonce")(HealthCheckResource(serviceConfig.systemConfig.instanceName).routes)
          }

        val bindingTask = Task.fromFuture(
          Http()
            .newServerAt(httpConfig.interface, httpConfig.port)
            .bind(routes)
        )

        context.pipeToSelf(bindingTask.timeout(10 seconds).runToFuture) {
          case Success(value) =>
            context.log.info(
              s"HTTP server bind successful to interface http://${httpConfig.interface}:${httpConfig.port}."
            )
            HttpServerBindingAvailable(value)
          case Failure(exception) =>
            context.log.error(
              s"HTTP server bind unsuccessful to interface http://${httpConfig.interface}:${httpConfig.port}.",
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
                  context.pipeToSelf(binding.terminate(httpConfig.gracefulShutdownTimeout)) {
                    case Success(_) =>
                      context.log.info("HTTP server shutdown successful")
                      Stop
                    case Failure(exception) =>
                      context.log.error("HTTP server shutdown failed", exception)
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
