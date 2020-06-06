package tech.canaryapp.server.config

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final case class Database(
  host: String,
  port: Int,
  user: String,
  pass: String,
  name: String,
  autoCommit: Option[Boolean],
  threadPool: Option[Int],
) {
  // default vaules
  val defaultThreadPool = 32
  val defaultAutoCommit = true

  // We supports only Postgresql
  val driver = "org.postgresql.Driver"
  val connectionString = s"jdbc:postgresql://$host:$port/$name"
}
