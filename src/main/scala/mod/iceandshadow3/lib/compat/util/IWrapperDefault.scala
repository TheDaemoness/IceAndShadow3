package mod.iceandshadow3.lib.compat.util

trait IWrapperDefault[T] extends IWrapper[T] {
	protected def expose(): T
	def isAny(queries: (T => Boolean)*): Boolean = {
		val what = expose()
		if(what == null) false
		else queries.exists(_(what))
	}
	def isAll(queries: (T => Boolean)*): Boolean = {
		val what = expose()
		if(what == null) false
		else queries.forall(_(what))
	}
}
