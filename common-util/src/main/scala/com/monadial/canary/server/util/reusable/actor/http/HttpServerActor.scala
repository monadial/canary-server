package com.monadial.canary.server.util.reusable.actor.http

import akka.actor.typed.Behavior
import akka.http.scaladsl.Http.ServerBinding
import com.monadial.canary.server.util.reusable.actor.http.config.HttpServerConfig
import com.monadial.canary.server.util.reusable.actor.http.service.HttpService

object HttpServerActor {
  type ServerProvider[CFG <: HttpServerConfig, HS <: HttpService[CFG]] = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message
  private[http] final case class HttpServerBindingAvailable(binding: ServerBinding) extends Message
}
