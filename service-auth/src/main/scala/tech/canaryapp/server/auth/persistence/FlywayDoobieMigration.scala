package tech.canaryapp.server.auth.persistence

import tech.canaryapp.server.auth.persistence.DoobieHikariTransactor.Transactor
import monix.eval.Task
import org.flywaydb.core.Flyway

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object FlywayDoobieMigration {

  def migrate(transactor: Transactor): Task[Unit] = transactor.configure { dataSource =>
    Task.eval {
      val flyway = Flyway.configure().dataSource(dataSource).load()
      flyway.migrate()
    }
  }
}
