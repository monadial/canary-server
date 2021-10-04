package com.monadial.canary.server.nonce.service.http

import akka.http.scaladsl.server.Route

trait HttpService {
  def routes: Route
}
