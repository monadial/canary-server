package tech.canaryapp.server.auth.actor.http

import akka.actor.typed.Behavior
import akka.http.scaladsl.Http.ServerBinding

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object HttpServerActor {

  type Provider = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message

  private[http] final case class HttpServerBindingAvailable(binding: ServerBinding) extends Message
}
