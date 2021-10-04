package com.monadial.canary.server.nonce.actor.nonce

import akka.actor.typed.scaladsl.Behaviors
import com.monadial.canary.server.nonce.actor.nonce.NonceActor
import com.monadial.canary.server.nonce.actor.nonce.NonceActor.Provider
import com.monadial.canary.server.nonce.actor.nonce.NonceActor.Stop
import com.monadial.canary.server.nonce.actor.nonce.service.NonceService

object NonceActorImpl {

  def createActor(nonceService: NonceService): Provider =
    () =>
      Behaviors.setup { context =>
        Behaviors.receiveMessagePartial {
          case Stop =>
            context.log.info("Stopping {}", context.self.path.toStringWithoutAddress)
            Behaviors.stopped
        }

      }
}
