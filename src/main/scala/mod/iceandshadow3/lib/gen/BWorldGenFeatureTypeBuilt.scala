package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.spatial.{IMap2d, IPosColumn}

abstract class BWorldGenFeatureTypeBuilt(
	val domain: BDomain, private val base: CanvasFeature
) extends BWorldGenFeatureType[TWorldGenColumnFn, TWorldGenColumnFn](base.xWidth, base.zWidth) {
	protected def baseCopy: CanvasFeature = base.copy
	/** Called to check if this feature instance differs from the base. */
	def instanceVariesFromBase(existing: IMap2d[TWorldGenColumnFn], origin: IPosColumn) = true
	/** Provide a non-base instance of a CanvasFeature in response to instanceVariesFromBase returning true. */
	def build(existing: IMap2d[TWorldGenColumnFn], origin: IPosColumn): CanvasFeature
	override def create(existing: IMap2d[TWorldGenColumnFn], origin: IPosColumn) = {
		if(instanceVariesFromBase(existing, origin)) build(existing, origin)
		else base
	}.create(existing, origin)
}
