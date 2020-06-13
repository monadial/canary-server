package tech.canaryapp.server.database

import com.typesafe.scalalogging.LazyLogging
import monix.eval.Task
import org.flywaydb.core.Flyway
import tech.canaryapp.server.database.DoobieHikariTransactor.Transactor

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object DoobieLiquibaseMigration extends LazyLogging {

  def initialize(transactor: Transactor): Task[Unit] = transactor.configure { dataSource =>
    Task.eval {
      val flyWay = Flyway.configure().dataSource(dataSource).load()
      flyWay.migrate()
    }
  }
}
