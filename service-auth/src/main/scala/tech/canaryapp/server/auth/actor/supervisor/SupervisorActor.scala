package tech.canaryapp.server.auth.actor.supervisor

import akka.Done
import akka.actor.typed.{ActorRef, Behavior}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object SupervisorActor {
  type Provider = () => Behavior[Message]

  sealed trait Message
  final case class Stop(replyTo: ActorRef[Done]) extends Message
}
