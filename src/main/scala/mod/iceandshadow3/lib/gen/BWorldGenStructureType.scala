package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.util.collect.{IMap2d, IRange2d, IRegion2d}

abstract class BWorldGenStructureType[
	+Column <: BWorldGenColumnFn,
	-ParentColumn <: BWorldGenColumnFn
](val xWidth: Int, val zWidth: Int) extends IRange2d {
	def apply(map: IMap2d[ParentColumn]): IMap2d[Column] with IRegion2d
}
