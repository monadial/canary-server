package tech.canaryapp.server.auth.actor.forwarder

import akka.actor.typed.scaladsl.Behaviors
import tech.canaryapp.server.auth.actor.forwarder.ForwarderActor.{Provider, Stop}
import tech.canaryapp.server.auth.config.AuthServiceConfig

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object ForwarderActorImpl {

  def createActor(config: AuthServiceConfig): Provider =
    () => Behaviors.setup { context =>

      Behaviors.receiveMessage {
        case Stop =>
          Behaviors.stopped
      }
    }
}
