package tech.canaryapp.server.actor

import tech.canaryapp.server.config.CanaryConfig
import tech.canaryapp.server.actor.supervisor.SupervisorActor
import tech.canaryapp.server.actor.supervisor.SupervisorActorImpl
import tech.canaryapp.server.actor.server.ServerActor
import tech.canaryapp.server.actor.server.ServerActorImpl
import com.softwaremill.macwire._
import tech.canaryapp.server.database.DatabaseModule

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ActorModule extends DatabaseModule{
  val configuration: CanaryConfig

  lazy val serverActor: ServerActor.Provider =
    wireWith(ServerActorImpl.createActor _)

  lazy val supervisorActor: SupervisorActor.Provider =
    wireWith(SupervisorActorImpl.createActor _)
}
