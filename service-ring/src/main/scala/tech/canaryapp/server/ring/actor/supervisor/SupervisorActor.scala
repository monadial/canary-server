package tech.canaryapp.server.ring.actor.supervisor

import akka.actor.typed.Behavior

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object SupervisorActor {

  type Provider = () => Behavior[Message]

  trait Message
  sealed case object Shutdown extends Message
}
