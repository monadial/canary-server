package tech.canaryapp.server.actor.server

import akka.actor.typed.Behavior
import akka.http.scaladsl.Http.ServerBinding

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object ServerActor {

  type Provider = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message

  private [server] final case class ServerBindingAvailable(binding: ServerBinding) extends Message
}
