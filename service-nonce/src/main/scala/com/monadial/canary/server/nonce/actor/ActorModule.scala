package com.monadial.canary.server.nonce.actor

import com.monadial.canary.server.nonce.actor.supervisor.{SupervisorActor, SupervisorActorImpl}
import com.monadial.canary.server.nonce.config.{NonceHttpServiceConfig, NonceServiceConfig}
import com.monadial.canary.server.nonce.service.http.NonceHttpService
import com.monadial.canary.server.util.reusable.actor.http.HttpServerActorImpl
import com.monadial.canary.server.nonce.config.NonceServiceConfig
import com.monadial.canary.server.util.reusable.actor.http.HttpServerActor.ServerProvider
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
