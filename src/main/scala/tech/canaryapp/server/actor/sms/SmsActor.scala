package tech.canaryapp.server.actor.sms

import akka.actor.typed.Behavior

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object SmsActor {

  type Provider = () => Behavior[Message]

  sealed trait Message
  case class SendMessage()
}
