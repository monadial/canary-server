package tech.canaryapp.server.auth.persistence

import com.typesafe.scalalogging.LazyLogging
import tech.canaryapp.server.auth.persistence.DoobieHikariTransactor.Transactor
import monix.eval.Task
import org.flywaydb.core.Flyway

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object FlywayDoobieMigration extends LazyLogging {

  def migrate(transactor: Transactor): Task[Unit] = transactor.configure { dataSource =>
    Task.eval {
      val flyway = Flyway.configure().dataSource(dataSource).load()
      val appliedMigrations = flyway.migrate()
      logger.info("Successfully applied " + appliedMigrations + " migrations")
    }
  }
}
