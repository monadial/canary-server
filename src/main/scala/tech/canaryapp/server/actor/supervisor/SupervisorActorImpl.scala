package tech.canaryapp.server.actor.supervisor

import akka.Done
import akka.actor.typed.Terminated
import akka.actor.typed.scaladsl.Behaviors
import tech.canaryapp.server.actor.supervisor.SupervisorActor.Message
import tech.canaryapp.server.actor.supervisor.SupervisorActor.Stop
import tech.canaryapp.server.config.CanaryConfig
import tech.canaryapp.server.actor.server.ServerActor

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object SupervisorActorImpl {

  def createActor(config: CanaryConfig, serverActorProvider: ServerActor.Provider): SupervisorActor.Provider =
    () =>
      Behaviors.setup { context =>
        // run server actor
        val serverActor = context.spawn(serverActorProvider(), "http-server")
        context.watch(serverActor)

        Behaviors.receiveMessagePartial[Message] {
          case Stop(replyTo) =>
            // stop child actors
            serverActor ! ServerActor.Stop

            Behaviors.receiveSignal {
              case (context, Terminated(_)) => {
                val children = context.children.toList
                if (children.isEmpty) {
                  context.log.info("Shutdown complete!")
                  replyTo ! Done
                  Behaviors.same
                } else {
                  context.log.debug("Child terminated, {} left.", children.length)
                  Behaviors.same
                }
              }
            }
        } receiveSignal {
          case (_, Terminated(ref)) =>
            context.log.error("Child ({}) terminated unexpectedly.", ref.path.toStringWithoutAddress)
            context.system.terminate()
            Behaviors.same
        }
      }
}
