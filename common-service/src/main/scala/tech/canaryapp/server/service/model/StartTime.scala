package tech.canaryapp.server.service.model

import java.time.temporal.ChronoUnit.{DAYS, HOURS, MINUTES, SECONDS}
import java.time.temporal.TemporalUnit
import java.time.{Instant, Duration => JDuration}
import java.util.Date

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class StartTime(nanoTime: Instant) extends AnyVal {
  def asLong: Long = nanoTime.getEpochSecond

  def asDate(): Date = Date.from(nanoTime)

  def uptime: JDuration = JDuration.ofSeconds(Instant.now().getEpochSecond - asLong)

  def formattedUptime: String = {
    val currentUptime = uptime

    Map[TemporalUnit, Long](
      SECONDS -> currentUptime.toSecondsPart,
      MINUTES -> currentUptime.toMinutesPart,
      HOURS -> currentUptime.toHoursPart,
      DAYS -> currentUptime.toDaysPart,
    ).map(c => s"${c._2} ${c._1.toString}")
      .toList
      .reverse
      .mkString("", ", ", ".")
  }
}

