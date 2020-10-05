package tech.canaryapp.server.channel.actor.supervisor

import akka.actor.typed.Behavior

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object SupervisorActor {

  type Provider = () => Behavior[Message]

  sealed trait Message
}
