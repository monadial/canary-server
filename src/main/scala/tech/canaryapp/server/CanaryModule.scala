package tech.canaryapp.server

import com.softwaremill.macwire._
import tech.canaryapp.server.actor.server.{ServerActor, ServerActorImpl}
import tech.canaryapp.server.actor.supervisor.{SupervisorActor, SupervisorActorImpl}
import tech.canaryapp.server.config.CanaryConfig
import tech.canaryapp.server.cryptography.CryptographyModule
import tech.canaryapp.server.transactor.DoobieHikariTransactor

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
trait CanaryModule extends CryptographyModule {
  val configuration: CanaryConfig
  val transactor: DoobieHikariTransactor.Transactor

  lazy val serverActor: ServerActor.Provider =
    wireWith(ServerActorImpl.createActor _)

  lazy val supervisorActor: SupervisorActor.Provider =
    wireWith(SupervisorActorImpl.createActor _)
}
