package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.{IMap2d, IPosColumn}
import mod.iceandshadow3.lib.util.collect.FixedMap2d

import scala.reflect.ClassTag

abstract class WorldGenFeatureTypeSimple[
	Column <: TWorldGenColumnFn: ClassTag,
	-ParentColumn <: TWorldGenColumnFn
](xWidth: Int, zWidth: Int) extends WorldGenFeatureType[Column, ParentColumn](xWidth, zWidth) {
	/** Return the column function at the specified relative coordinate. */
	def columnAt(xRela: Int, zRela: Int, parent: ParentColumn): Column
	override def create(existing: IMap2d[ParentColumn], origin: IPosColumn) = new FixedMap2d[Column](
		origin.xBlock, origin.zBlock, xWidth, zWidth,
		(x, z) => columnAt(x - origin.xBlock, z - origin.zBlock, existing(x, z))
	)
}
