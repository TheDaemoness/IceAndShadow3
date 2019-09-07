package mod.iceandshadow3.lib.util

abstract class BFunctionOptions[-In, Med, +Out] (
	protected val fns: (In => Med)*
) extends (In => Out) {
	def apply(what: In) = {
		for(fn <- fns) {
			val med = fn(what)
			if(!discard(med)) transform(med)
		}
		transform(default)
	}
	protected def discard(what: Med): Boolean
	protected def transform(what: Med): Out
	protected def default: Med
}
