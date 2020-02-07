package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.{IMap2d, IPosColumn}

abstract class WorldGenFeatureTypeBuilt[-ParentColumn <: TWorldGenColumnFn] (
	private val base: CanvasFeature
) extends WorldGenFeatureType[TWorldGenColumnFn, ParentColumn](base.xWidth, base.zWidth) {
	type Analysis
	protected def makeBaseCopy: CanvasFeature = base.copy
	/** Evaluate the parent worldgen layer. If null is returned, the base CanvasFeature is used. */
	def analyze(existing: IMap2d[ParentColumn], origin: IPosColumn): Analysis
	/** Provide a non-base instance of a CanvasFeature in response to non-null analysis. */
	def build(analysis: Analysis): CanvasFeature
	override def create(existing: IMap2d[ParentColumn], origin: IPosColumn) = {
		val analysis = analyze(existing, origin)
		if(analysis != null) build(analysis)
		else base
	}.create(existing, origin)
}
