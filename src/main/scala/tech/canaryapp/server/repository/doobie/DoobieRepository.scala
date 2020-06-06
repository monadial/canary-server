package tech.canaryapp.server.repository.doobie

import monix.eval.Task
import tech.canaryapp.server.repository.{Entity, Identifier, Repository}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
trait DoobieRepository[E <: Entity, I <: Identifier[_]] extends Repository[Task, E, I]
