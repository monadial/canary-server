package tech.canaryapp.server.ring.actor

import tech.canaryapp.server.ring.actor.supervisor.{SupervisorActor, SupervisorActorImpl}
import tech.canaryapp.server.ring.config.RingServiceConfig
import tech.canaryapp.server.service.config.Config

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ActorModule {
  val moduleConfig: Config[RingServiceConfig]

  lazy val supervisorActor: SupervisorActor.Provider =
    SupervisorActorImpl.createActor()
}
