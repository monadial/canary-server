package tech.canaryapp.server.actor.crypto

import akka.actor.typed.Behavior

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object CryptoActor {

  type Provider = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message
}
