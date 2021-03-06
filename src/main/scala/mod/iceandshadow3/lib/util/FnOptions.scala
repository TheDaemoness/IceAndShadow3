package mod.iceandshadow3.lib.util

abstract class FnOptions[-In, @specialized(Boolean) Med, @specialized(Boolean) +Out] (
	protected val fns: (In => Med)*
) extends (In => Out) {
	def apply(what: In): Out = {
		for(fn <- fns) {
			val med = fn(what)
			if(!discard(med)) return transform(med)
		}
		transform(default)
	}
	protected def discard(what: Med): Boolean
	protected def transform(what: Med): Out
	protected def default: Med
}
