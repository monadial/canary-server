package com.monadial.canary.server.auth.persistence

import cats.effect.{Blocker, Resource}
import com.monadial.canary.server.auth.config.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import monix.eval.Task

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object DoobieHikariTransactor {

  type Transactor = HikariTransactor[Task]

  lazy val instance: DatabaseConfig => Resource[Task, Transactor] = { config =>
    lazy val hc = new HikariConfig {
      hikariConfig =>
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
