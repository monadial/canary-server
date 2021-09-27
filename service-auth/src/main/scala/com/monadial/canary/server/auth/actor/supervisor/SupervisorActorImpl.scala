package com.monadial.canary.server.auth.actor.supervisor

import akka.Done
import akka.actor.typed.Terminated
import akka.actor.typed.scaladsl.Behaviors
import com.monadial.canary.server.auth.actor.consumer.ConsumerActor
import com.monadial.canary.server.auth.actor.http.HttpServerActor
import com.typesafe.scalalogging.LazyLogging
import SupervisorActor.{Message, Provider, Stop}
import com.monadial.canary.server.auth.actor.forwarder.ForwarderActor

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object SupervisorActorImpl extends LazyLogging {

  def createActor(
    forwarderActorProvider: ForwarderActor.Provider,
    consumerActorProvider: ConsumerActor.Provider,
    httpActorProvider: HttpServerActor.Provider
  ): Provider =
    () =>
      Behaviors.setup { context =>
        // forwarder
        val forwarderActor = context.spawn(forwarderActorProvider(), "forwarder")
        context.watch(forwarderActor)

        // consumer
        val consumerActor = context.spawn(consumerActorProvider(forwarderActor), "consumer")
        context.watch(consumerActor)

        // http server
        val httpServerActor = context.spawn(httpActorProvider(), "http-server")
        context.watch(httpServerActor)

        Behaviors.receiveMessagePartial[Message] {
          case Stop(replyTo) =>
            consumerActor ! ConsumerActor.Stop
            httpServerActor ! HttpServerActor.Stop
            forwarderActor ! ForwarderActor.Stop // must stopped last

            Behaviors.receiveSignal {
              case (context, Terminated(ref)) =>
                val children = context.children.toList

                if (children.isEmpty) {
                  logger.info("Service supervisor shutdown completed.")
                  replyTo ! Done
                  Behaviors.same
                } else {
                  logger.debug(
                    "Supervisor child ({}) terminated, {} child left.",
                    ref.path.toStringWithoutAddress,
                    children.length
                  )
                  Behaviors.same
                }
            }
        } receiveSignal {
          case (context, Terminated(ref)) =>
            logger.error("Supervisor child {} terminated unexpectedly.", ref.path.toStringWithoutAddress)
            context.system.terminate()
            Behaviors.same
        }
      }

}
