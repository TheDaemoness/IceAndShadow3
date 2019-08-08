package mod.iceandshadow3.lib.util

/** Indirection through boxes. */
final class BoxedVar[T] {
	private var v: T = _
	implicit def get: T = v
	def update(newvalue: T): Unit = {v = newvalue}
}

/** Boxed Scala-flavored laziness. */
final class BoxedLazy[+T](fn: => T) {
	implicit lazy val get: T = fn
}
