package tech.canaryapp.server.actor.server.http
import java.util.Base64

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import monix.eval.Task
import monix.execution.Scheduler
import tech.canaryapp.server.action.ActionDispatcher
import tech.canaryapp.server.action.ActionModule
import tech.canaryapp.server.action.actions.cryptography.GenerateEc25519KeyPairAction

import scala.util.Failure
import scala.util.Success

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final class HomeEndpoint(actionModule: ActionModule) extends Endpoint {

  private val dispatcher = actionModule.actionDispatcher

  private implicit val scheduler: Scheduler = Scheduler.global

  override def routes: Route = pathSingleSlash {
    get {
      complete("hello")
    }
  }
}
