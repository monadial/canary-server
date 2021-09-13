package tech.canaryapp.server.util.reusable.actor.http

import tech.canaryapp.server.util.reusable.actor.http.config.HttpServerConfig
import tech.canaryapp.server.util.reusable.actor.http.service.HttpService
import akka.actor.typed.Behavior
import akka.http.scaladsl.Http.ServerBinding

object HttpServerActor {
  type ServerProvider[CFG <: HttpServerConfig, HS <: HttpService[CFG]] = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message
  private[http] final case class HttpServerBindingAvailable(binding: ServerBinding) extends Message
}
