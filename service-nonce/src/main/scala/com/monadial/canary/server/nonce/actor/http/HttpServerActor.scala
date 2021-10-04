package com.monadial.canary.server.nonce.actor.http

import akka.actor.typed.{ActorRef, Behavior}
import akka.http.scaladsl.Http.ServerBinding
import com.monadial.canary.server.nonce.actor.nonce.NonceActor

object HttpServerActor {

  type Provider = (ActorRef[NonceActor.Message]) => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message
  private[http] final case class HttpServerBindingAvailable(binding: ServerBinding) extends Message
}
