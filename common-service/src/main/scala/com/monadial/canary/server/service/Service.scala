package com.monadial.canary.server.service

import com.monadial.canary.server.service.config.Config
import com.monadial.canary.server.service.model.{ClusterName, DefaultConfigImpl, InstanceName, ServiceName, StartTime}

import java.time.Instant
import com.typesafe.config.{ConfigFactory, Config => TConfig}
import com.typesafe.scalalogging.LazyLogging
import kamon.Kamon
import monix.eval.Task
import monix.execution.Scheduler
import com.monadial.canary.server.service.model._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait Service extends LazyLogging {

  protected val serviceName: ServiceName

  // must not be lazy
  final protected val startTime: StartTime = StartTime(Instant.now())

  final protected lazy val clusterName: ClusterName = ClusterName
    .readFromEnvironment
    .read()
    .get

  final protected lazy val instanceName: InstanceName =
    InstanceName(clusterName, serviceName, startTime)

  // todo refactor load root config from zookeper
  final private lazy val rootConfig: Task[TConfig] =
    Task.now(ConfigFactory.load())

  final private lazy val serviceMetricsCollector: ServiceMetricsCollector =
    new ServiceMetricsCollectorImpl

  final def main(args: Array[String]): Unit = {
    Kamon.init()
    val cancelableFuture = rootConfig
      .map(c => DefaultConfigImpl(c, clusterName, instanceName, serviceName))
      .flatMap { config =>
        println(ServiceBanner(instanceName).banner)
        val runService = start(config)
        val runKamonCollector = serviceMetricsCollector
          .start()
          .guarantee(Task.eval(Kamon.stopModules()))

        Task.race(runKamonCollector, runService)
      }
      .runToFuture(Scheduler.global)

    Try(Await.result(cancelableFuture, Duration.Inf)) match {
      case Failure(error) =>
        error.printStackTrace(System.err)
        logger.error(s"Canary service ${serviceName.name} failed @ ${startTime.asDate().toString}.", error)
        logger.error(s"Service uptime: ${startTime.formattedUptime}")
        logger.error(s"Cluster service: ${serviceName.name}, member: " + instanceName.toString + " failed.")
        System.exit(1)
      case Success(_) =>
        logger.info(s"Canary service ${serviceName.name} ended @ ${startTime.asDate().toString}. ")
        logger.info(s"Service uptime: ${startTime.formattedUptime}")
        logger.info(s"Cluster service: ${serviceName.name}, member: " + instanceName.toString + "  ended.")
        System.exit(0)
    }
  }

  protected def start(serviceConfig: Config): Task[Unit]
}
