package tech.canaryapp.config

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
final case class CanaryConfig(
  httpServer: HttpServer,
  cryptography: Cryptography
)
