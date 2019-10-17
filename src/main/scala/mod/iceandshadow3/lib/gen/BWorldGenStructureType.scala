package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.IPosColumn
import mod.iceandshadow3.lib.util.collect.{IMap2d, IRange2d, IRegion2d}

abstract class BWorldGenStructureType[
	+Column <: TWorldGenColumnFn,
	-ParentColumn <: TWorldGenColumnFn
](val xWidth: Int, val zWidth: Int) extends IRange2d {
	def create(map: IMap2d[ParentColumn], origin: IPosColumn): IMap2d[Column] with IRegion2d
}
