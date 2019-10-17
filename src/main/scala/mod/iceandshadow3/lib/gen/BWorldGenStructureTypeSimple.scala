package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.IPosColumn
import mod.iceandshadow3.lib.util.collect.{FixedMap2d, IMap2d}

import scala.reflect.ClassTag

abstract class BWorldGenStructureTypeSimple[
	Column <: TWorldGenColumnFn: ClassTag,
	-ParentColumn <: TWorldGenColumnFn
](xWidth: Int, zWidth: Int) extends BWorldGenStructureType[Column, ParentColumn](xWidth, zWidth) {
	def columnAt(xRela: Int, zRela: Int, parent: ParentColumn): Column
	override def create(map: IMap2d[ParentColumn], origin: IPosColumn) = new FixedMap2d[Column](
		origin.xBlock, origin.zBlock, xWidth, zWidth,
		(x, z) => columnAt(x - origin.xBlock, z - origin.zBlock, map(x, z))
	)
}