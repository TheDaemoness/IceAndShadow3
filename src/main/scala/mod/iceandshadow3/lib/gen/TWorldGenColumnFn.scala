package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain

/** Data holder and modifier of WorldGenColumns. */
trait TWorldGenColumnFn {
	def domain: BDomain
	def apply(to: WorldGenColumn): Unit
}
object TWorldGenColumnFn {
	def noOp(dom: BDomain) = new TWorldGenColumnFn {
		override def domain = dom
		override def apply(v1: WorldGenColumn): Unit = ()
	}
}

//TODO: We can develop more intuitive ways of sending data between layers.
// The idea is to have each gen layer provide a function of position and some ColumnData object.
// ColumnData is an expanding view into an array-turned-flat-map,
// its keys BVar objects returned by each layer to the world gen which assigns them numeric ids (binder-esquely)
// This requires EVEN MORE worldgen rearchitecting and introduces a somewhat painful concurrency problem.
// May explore later in a separate branch.
