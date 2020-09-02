package tech.canaryapp.server.service

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
trait ReadFromEnvironment[A] {
  def read(): Option[A]
}
