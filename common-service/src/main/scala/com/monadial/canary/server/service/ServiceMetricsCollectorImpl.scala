package com.monadial.canary.server.service

import java.lang.management.ManagementFactory

import kamon.Kamon
import kamon.metric.Gauge
import monix.eval.Task
import monix.execution.Cancelable

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final class ServiceMetricsCollectorImpl extends ServiceMetricsCollector {
  private def memoryCommittedGauge(region: String): Gauge =
    Kamon.gauge("jvm_memory_committed_bytes").withTag("region", region)

  private def memoryUsedGauge(region: String): Gauge =
    Kamon.gauge("jvm_memory_used_bytes").withTag("region", region)

  override def start(): Task[Unit] = Task.create { (scheduler, callback) =>
    def nextPoll: Cancelable = {
      scheduler.scheduleOnce(5 seconds) {
        try {
          collectMetrics()
          nextPoll
        } catch {
          case e: Throwable =>
            callback.onError(e)
        }
      }
    }

    nextPoll
  }

  private def collectMetrics(): Unit = {
    // memory
    val memBean = ManagementFactory.getMemoryMXBean
    memoryUsedGauge("heap").update(memBean.getHeapMemoryUsage.getUsed.toDouble)
    memoryUsedGauge("non-heap").update(memBean.getNonHeapMemoryUsage.getUsed.toDouble)
    memoryCommittedGauge("heap").update(memBean.getHeapMemoryUsage.getCommitted.toDouble)
    memoryCommittedGauge("non-heap").update(memBean.getNonHeapMemoryUsage.getCommitted.toDouble)
  }
}
