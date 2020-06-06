package tech.canaryapp.server.database

import cats.effect.Resource
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.log.LogHandler
import monix.eval.Task
import tech.canaryapp.server.config.Database
import cats.effect.Blocker
import cats.syntax.applicative._
import com.zaxxer.hikari.HikariConfig

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object DoobieHikariTransactor {

  val defaultThreadPool = 32

  lazy val instance: Database => Resource[Task, HikariTransactor[Task]] = { config =>
    lazy val hc = new HikariConfig { hikariConfig =>
      hikariConfig setDriverClassName config.driver
      hikariConfig setJdbcUrl config.connectionString
      hikariConfig setAutoCommit config.autoCommit.getOrElse(config.defaultAutoCommit)
      hikariConfig setUsername config.user
      hikariConfig setPassword config.pass
    }

    for {
      ec <- ExecutionContexts.fixedThreadPool[Task](config.threadPool.getOrElse(config.defaultThreadPool))
      bl <- Blocker[Task]
      rx <- HikariTransactor.fromHikariConfig[Task](hc, ec, bl)
    } yield rx
  }
}
