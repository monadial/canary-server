package tech.canaryapp.server.auth.actor.forwarder

import akka.actor.typed.Behavior

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object ForwarderActor {

  type Provider = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message
}
