package tech.canaryapp.server.ring.actor.supervisor

import akka.actor.typed.scaladsl.Behaviors
import tech.canaryapp.server.ring.actor.supervisor.SupervisorActor.Message

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object SupervisorActorImpl {

  def createActor(): SupervisorActor.Provider = () =>
    Behaviors.empty[Message]
}
