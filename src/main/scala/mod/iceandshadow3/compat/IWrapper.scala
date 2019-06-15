package mod.iceandshadow3.compat

trait IWrapper[T] {
	protected[compat] def expose(): T
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
