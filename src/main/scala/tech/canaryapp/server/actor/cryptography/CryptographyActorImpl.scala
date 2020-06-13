package tech.canaryapp.server.actor.cryptography

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import tech.canaryapp.server.actor.cryptography.CryptographyActor.GenerateKeyPair
import tech.canaryapp.server.actor.cryptography.CryptographyActor.Message
import tech.canaryapp.server.actor.cryptography.CryptographyActor.Stop
import tech.canaryapp.server.cryptography.ellipticCurve.EllipticCurve

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object CryptographyActorImpl {

  def createActor(ellipticCurve: EllipticCurve): CryptographyActor.Provider =
    () =>
      Behaviors.setup { ctx =>
        def activeBehavior: Behavior[Message] = Behaviors.receiveMessage[Message] {
          case Stop =>
            Behaviors.stopped
          case GenerateKeyPair(replyTo) =>
            replyTo ! ellipticCurve.generateKeyPair
            Behaviors.same
        }

        activeBehavior
      }

}
