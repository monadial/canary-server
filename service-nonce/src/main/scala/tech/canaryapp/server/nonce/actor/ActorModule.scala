package tech.canaryapp.server.nonce.actor

import tech.canaryapp.server.nonce.config.{NonceHttpServiceConfig, NonceServiceConfig}
import tech.canaryapp.server.nonce.service.http.NonceHttpService
import tech.canaryapp.server.util.reusable.actor.http.HttpServerActor.ServerProvider
import tech.canaryapp.server.util.reusable.actor.http.HttpServerActorImpl
import tech.canaryapp.server.nonce.actor.supervisor.SupervisorActor
import tech.canaryapp.server.nonce.actor.supervisor.SupervisorActorImpl
import com.softwaremill.macwire._

trait ActorModule {
  type HttpServiceProvider = ServerProvider[NonceHttpServiceConfig, NonceHttpService]

  val config: NonceServiceConfig

  lazy val httpServiceConfig: NonceHttpServiceConfig = config.httpServiceConfig

  lazy val nonceHttpService: NonceHttpService = wire[NonceHttpService]

  lazy val httpActorProvider: HttpServiceProvider  = wireWith(
    HttpServerActorImpl.createActor[NonceHttpServiceConfig, NonceHttpService] _
  )
  lazy val supervisorActorProvider: SupervisorActor.Provider = wireWith(SupervisorActorImpl.createActor _)
}
