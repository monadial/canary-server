package tech.canaryapp.server.config

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class CanaryConfig(
  httpServer: HttpServer,
  cryptography: Cryptography
)
