package tech.canaryapp.server

import com.softwaremill.macwire._
import tech.canaryapp.server.action.actions.cryptography.{GenerateEc25519KeyPairAction, GenerateEc25519KeyPairActionHandler}
import tech.canaryapp.server.action.{ActionHandler, ActionModule}
import tech.canaryapp.server.actor.server.http.Endpoint
import tech.canaryapp.server.actor.server.http.HomeEndpoint
import tech.canaryapp.server.actor.server.ServerActor
import tech.canaryapp.server.actor.server.ServerActorImpl
import tech.canaryapp.server.actor.supervisor.SupervisorActor
import tech.canaryapp.server.actor.supervisor.SupervisorActorImpl
import tech.canaryapp.server.config.CanaryConfig
import tech.canaryapp.server.cryptography.CryptographyModule
import tech.canaryapp.server.database.DoobieHikariTransactor

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
trait CanaryModule extends CryptographyModule with ActionModule {
  val actionModule: ActionModule = this

  val configuration: CanaryConfig
  val transactor: DoobieHikariTransactor.Transactor

  override protected lazy val generateEc25519KeyPairActionHandler: ActionHandler[GenerateEc25519KeyPairAction] =
    wire[GenerateEc25519KeyPairActionHandler]

  lazy val serverActor: ServerActor.Provider =
    wireWith(ServerActorImpl.createActor _)

  lazy val supervisorActor: SupervisorActor.Provider =
    wireWith(SupervisorActorImpl.createActor _)

  lazy val endopints: List[Endpoint] =
    wire[HomeEndpoint] :: Nil

}
