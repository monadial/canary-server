package tech.canaryapp.server.repository

import monix.eval.Task

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait Repository[F[_], E <: Entity, I <: Identifier[_]] {

  def get(id: I): F[E]

  def find(id: I): F[Option[E]]

  def save(entity: E): F[Int]

  def delete(id: I): F[Int]

  def update(id: I, entity: E): F[Int]
}
