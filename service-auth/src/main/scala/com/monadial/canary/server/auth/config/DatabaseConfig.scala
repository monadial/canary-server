package com.monadial.canary.server.auth.config

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait DatabaseConfig {
  val host: String
  val port: Int
  val user: String
  val pass: String
  val name: String
  val autoCommit: Option[Boolean]
  val threadPool: Option[Int]

  val defaultThreadPool = 32
  val defaultAutoCommit = false

  // We supports only Postgresql
  val driver = "org.postgresql.Driver"

  lazy val connectionString = s"jdbc:postgresql://$host:$port/$name"
}
