package tech.canaryapp.server.config

import scala.concurrent.duration.FiniteDuration

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final case class CanaryConfig(
    gracefulShutdownTimeout: FiniteDuration,
    httpServer: HttpServer,
    cryptography: Cryptography,
    database: Database,
    twillio: Twillio
)
