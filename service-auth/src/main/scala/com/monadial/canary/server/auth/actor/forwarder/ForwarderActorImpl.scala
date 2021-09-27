package com.monadial.canary.server.auth.actor.forwarder

import akka.actor.typed.scaladsl.Behaviors
import ForwarderActor.{Provider, Stop}
import com.monadial.canary.server.auth.config.AuthServiceConfig

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
