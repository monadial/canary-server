package com.monadial.canary.server.auth.actor

import akka.kafka.CommitterSettings
import com.monadial.canary.server.auth.actor.consumer.{ConsumerActor, ConsumerActorImpl}
import com.monadial.canary.server.auth.actor.forwarder.{ForwarderActor, ForwarderActorImpl}
import com.monadial.canary.server.auth.actor.http.{HttpServerActor, HttpServerActorImpl}
import com.monadial.canary.server.auth.actor.supervisor.{SupervisorActor, SupervisorActorImpl}
import com.monadial.canary.server.auth.config.AuthServiceConfig
import com.softwaremill.macwire._
import monix.execution.Scheduler
import org.apache.kafka.clients.producer.Producer
import com.monadial.canary.server.auth.actor.http.HttpServerActor
import com.monadial.canary.server.auth.actor.supervisor.SupervisorActorImpl
import com.monadial.canary.server.auth.actor.consumer.ConsumerActor
import com.monadial.canary.server.auth.actor.forwarder.ForwarderActorImpl
import com.monadial.canary.server.auth.persistence.DoobieHikariTransactor.Transactor

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ActorModule {
  val config: AuthServiceConfig

  val transactor: Transactor

  val kafkaProducer: Producer[Array[Byte], Array[Byte]]

  val scheduler: Scheduler

  lazy val committerSettings: CommitterSettings = config.kafkaCommitterSettings

  lazy val forwarderActorProvider: ForwarderActor.Provider = wireWith(ForwarderActorImpl.createActor _)

  lazy val httpActorProvider: HttpServerActor.Provider = wireWith(HttpServerActorImpl.createActor _)

  lazy val consumerActorProvider: ConsumerActor.Provider = wireWith(ConsumerActorImpl.createActor _)

  lazy val supervisorActorProvider: SupervisorActor.Provider = wireWith(SupervisorActorImpl.createActor _)
}
