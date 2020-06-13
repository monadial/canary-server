package tech.canaryapp.server.actor.server.http

import akka.http.scaladsl.server.Route

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait Endpoint {

  def routes: Route
}
