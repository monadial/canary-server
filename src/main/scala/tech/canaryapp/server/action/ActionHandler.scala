package tech.canaryapp.server.action

import monix.eval.Task

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
trait ActionHandler[A <: Action] {

  def handle(action: A): Task[A#Out]
}
