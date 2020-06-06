package tech.canaryapp.server.database

import doobie.implicits._
import cats.effect._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import monix.eval.Task
import monix.execution.Scheduler
import tech.canaryapp.server.config.CanaryConfig
import cats.Monad
import monix.eval.Task

import scala.language.postfixOps

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
trait DatabaseModule {

  val configuration: CanaryConfig
  val transactor: HikariTransactor[Task]
}
