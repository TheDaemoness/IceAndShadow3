package mod.iceandshadow3.lib.util

class Is[-T] protected(
	wanted: Boolean, invert: Boolean,
	preds: (T => Boolean)*
) extends FnOptions[T, Boolean, Boolean] (preds:_*) {
	override protected def discard(what: Boolean) = what != wanted
	override protected def transform(what: Boolean) = if(invert) !what else what
	override protected def default = !wanted
	def unapply(what: T): Boolean = apply(what)
}

object Is {
	def any[T](preds: (T => Boolean)*): Is[T] = new Is[T](true, false, preds:_*)
	def all[T](preds: (T => Boolean)*): Is[T] = new Is[T](false, false, preds:_*)
	def none[T](preds: (T => Boolean)*): Is[T] = new Is[T](true, true, preds:_*)
}