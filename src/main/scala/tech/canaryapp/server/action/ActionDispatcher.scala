package tech.canaryapp.server.action

import monix.eval.Task
import shapeless.HList
import shapeless.ops.hlist

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
final class ActionDispatcher[L <: HList](private val handlers: L) {

  def dispatch[A <: Action](action: A)(implicit s: hlist.Selector[L, ActionHandler[A]]): Task[A#Out] =
    s(handlers).handle(action)
}

object ActionDispatcher {

  def of[L <: HList](handlers: L) = new ActionDispatcher[L](handlers)
}
