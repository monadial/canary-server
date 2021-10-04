package com.monadial.canary.server.nonce.actor

import com.monadial.canary.server.nonce.actor.supervisor.{SupervisorActor, SupervisorActorImpl}
import com.monadial.canary.server.nonce.actor.http.{HttpServerActor, HttpServerActorImpl}
import com.monadial.canary.server.nonce.actor.nonce.service.{NonceService, RedisNonceService}
import com.monadial.canary.server.nonce.actor.nonce.{NonceActor, NonceActorImpl}
import com.monadial.canary.server.nonce.config.{NonceHttpServiceConfig, NonceServiceConfig}
import com.monadial.canary.server.service.model.InstanceName
import com.softwaremill.macwire._

trait ActorModule {

  val config: NonceServiceConfig

  lazy val nonceService: NonceService = wire[RedisNonceService]

  lazy val nonceActor: NonceActor.Provider = wireWith(NonceActorImpl.createActor _)

  lazy val httpServerActor: HttpServerActor.Provider = wireWith(HttpServerActorImpl.createActor _)

  lazy val supervisorActorProvider: SupervisorActor.Provider = wireWith(SupervisorActorImpl.createActor _)
}
