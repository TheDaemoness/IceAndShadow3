package mod.iceandshadow3.util

import java.util.function._

/*
 * This file defines implementations of the interfaces in java.util.function that each take a Scala function.
 * It will grow based on what is needed or likely to be needed.
 */

abstract class FuncWrapper[F] (protected val fn: F)

class FnBiConsumer[A,B](override val fn: (A,B) => Unit) extends FuncWrapper(fn) with BiConsumer[A,B] {
	override def accept(a: A, b: B): Unit = fn(a,b)
}
class FnBiFunction[A,B,R](override val fn: (A,B) => R) extends FuncWrapper(fn) with BiFunction[A,B,R] {
	override def apply(a: A, b: B): R = fn(a,b)
}