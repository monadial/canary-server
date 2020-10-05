package tech.canaryapp.server.auth.actor.consumer

import akka.actor.typed.{ActorRef, Behavior}
import tech.canaryapp.server.auth.actor.forwarder.ForwarderActor

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object ConsumerActor {

  type Provider = ActorRef[ForwarderActor.Message] => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message
}
