package tech.canaryapp.server.auth.actor.supervisor

import akka.Done
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, Terminated}
import monix.eval.Task
import tech.canaryapp.server.auth.actor.supervisor.SupervisorActor.{Message, Provider, Stop}
import tech.canaryapp.server.auth.actor.http.HttpServerActor

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object SupervisorActorImpl {

  def createActor(
    httpActorProvider: HttpServerActor.Provider
  ): Provider =
    () =>
      Behaviors.setup { context =>
        // http server
        val httpServerActor = context.spawn(httpActorProvider(), "http-server")
        context.watch(httpServerActor)

        Behaviors.receiveMessagePartial[Message] {
          case Stop(replyTo) =>
            httpServerActor ! HttpServerActor.Stop

            Behaviors.receiveSignal {
              case (context, Terminated(ref)) =>
                val children = context.children.toList

                if (children.isEmpty) {
                  context.log.info("Service supervisor shutdown completed.")
                  replyTo ! Done
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
