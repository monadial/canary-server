package com.monadial.canary.server.nonce.actor.supervisor

import akka.Done
import akka.actor.typed.Terminated
import akka.actor.typed.scaladsl.Behaviors
import com.monadial.canary.server.nonce.actor.supervisor.SupervisorActor.Message
import com.monadial.canary.server.util.reusable.actor.http.HttpServerActor
import com.monadial.canary.server.util.reusable.actor.http.HttpServerActor.Stop
import com.typesafe.scalalogging.LazyLogging
import SupervisorActor.{Message, Provider, Stop}
import com.monadial.canary.server.nonce.actor.ActorModule

object SupervisorActorImpl extends LazyLogging {

  def createActor(
      httpServiceActorProvider: ActorModule#HttpServiceProvider
  ): Provider = () => Behaviors.setup { context =>
    // http server
    val httpServerActor = context.spawn(httpServiceActorProvider(), "http-server")
    context.watch(httpServerActor)

    Behaviors.receiveMessagePartial[Message] {
      case Stop(replyTo) =>
        httpServerActor ! Stop

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
