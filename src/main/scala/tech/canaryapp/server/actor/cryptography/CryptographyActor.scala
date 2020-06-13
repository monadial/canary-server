package tech.canaryapp.server.actor.cryptography

import akka.actor.typed.{ActorRef, Behavior}
import tech.canaryapp.server.cryptography._

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object CryptographyActor {


  type Provider = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message

  final case class GenerateKeyPair(replyTo: ActorRef[KeyPair]) extends Message
}
