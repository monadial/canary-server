package tech.canaryapp.server.model.value

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait ValueObject[T] extends Serializable {
  protected val value: T

  def sameValueAs(other: T): Boolean
}
