package com.monadial.canary.server.nonce.actor.nonce

import akka.actor.typed.Behavior
import com.monadial.canary.server.nonce.actor.http.HttpServerActor.Message

object NonceActor {

  type Provider = () => Behavior[Message]

  sealed trait Message
  final case object Stop extends Message
}
