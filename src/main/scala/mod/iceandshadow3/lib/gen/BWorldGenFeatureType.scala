package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.{IMap2d, IPosColumn, IRange2d, IRegion2d}

abstract class BWorldGenFeatureType[
	+Column <: TWorldGenColumnFn,
	-ParentColumn <: TWorldGenColumnFn
](val xWidth: Int, val zWidth: Int) extends IRange2d {
	/** Construct a grid of columns to be applied to the world.
		* The existing map is guaranteed to be valid from origin to origin+<xWidth,zWidth>. */
	def create(existing: IMap2d[ParentColumn], origin: IPosColumn): IMap2d[Column] with IRegion2d
}
