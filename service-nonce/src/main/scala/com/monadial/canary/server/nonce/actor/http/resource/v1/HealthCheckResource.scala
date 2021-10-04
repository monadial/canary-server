package com.monadial.canary.server.nonce.actor.http.resource.v1

import akka.http.scaladsl.server.Route
import com.monadial.canary.server.nonce.actor.http.resource.Resource
import akka.http.scaladsl.server.Directives._
import com.monadial.canary.server.service.model.InstanceName

final class HealthCheckResource(instanceName: InstanceName) extends Resource {
  override val routes: Route = complete(instanceName.toString)
}

object HealthCheckResource {
  def apply(instanceName: InstanceName): HealthCheckResource = new HealthCheckResource(instanceName)
}
