package com.monadial.canary.server.nonce.actor.supervisor

import akka.Done
import akka.actor.typed.Terminated
import akka.actor.typed.eventstream.EventStream.{Publish, Subscribe}
import akka.actor.typed.scaladsl.Behaviors
import com.monadial.canary.server.nonce.actor.supervisor.SupervisorActor.{Message, Provider, Stop}
import com.monadial.canary.server.nonce.actor.http.HttpServerActor
import com.monadial.canary.server.nonce.actor.nonce.NonceActor
import com.typesafe.scalalogging.LazyLogging

object SupervisorActorImpl {

  def createActor(
      nonceActorProvider: NonceActor.Provider,
      httpActorProvider: HttpServerActor.Provider
  ): Provider = () => Behaviors.setup { context =>
    // nonce actor
    val nonceActor = context.spawn(nonceActorProvider(), "nonce")
    context.watch(nonceActor)

    // http server
    val httpServerActor = context.spawn(httpActorProvider(nonceActor), "http-server")
    context.watch(httpServerActor)



    Behaviors.receiveMessagePartial[Message] {
      case Stop(replyTo) =>
        nonceActor ! NonceActor.Stop
        httpServerActor ! HttpServerActor.Stop

      Behaviors.receiveSignal {
        case (context, Terminated(ref)) =>
          val children = context.children.toList

        if (children.isEmpty) {
          context.log.info("Service supervisor shutdown completed.")
          replyTo ! Done.done()
          Behaviors.same
        } else {
          context.log.debug(
            "Supervisor child ({}) terminated, {} child left.",
            ref.path.toStringWithoutAddress,
            children.length
          )
          Behaviors.same
        }
      }
    } receiveSignal {
      case (context, Terminated(ref)) =>
        context.log.error("Supervisor child {} terminated unexpectedly.", ref.path.toStringWithoutAddress)
        context.system.terminate()
        Behaviors.same
    }




  }
}
