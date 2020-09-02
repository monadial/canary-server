package tech.canaryapp.server.auth.actor

import com.softwaremill.macwire._
import org.apache.kafka.clients.producer.Producer
import tech.canaryapp.server.auth.actor.http.{HttpServerActor, HttpServerActorImpl}
import tech.canaryapp.server.auth.actor.supervisor.{SupervisorActor, SupervisorActorImpl}
import tech.canaryapp.server.auth.config.AuthServiceConfig
import tech.canaryapp.server.auth.persistence.DoobieHikariTransactor.Transactor
import tech.canaryapp.server.service.config.Config

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ActorModule {
  val config: AuthServiceConfig

  val transactor: Transactor

  val kafkaProducer: Producer[Array[Byte], Array[Byte]]

  lazy val httpActorProvider: HttpServerActor.Provider = wireWith(HttpServerActorImpl.createActor _)

  lazy val supervisorActorProvider: SupervisorActor.Provider = wireWith(SupervisorActorImpl.createActor _)
}
