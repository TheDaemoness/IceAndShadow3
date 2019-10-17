package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.spatial.IPosColumn
import mod.iceandshadow3.lib.util.collect.{IMap2d, IRegion2d}

trait TWorldGenLayer[Column <: TWorldGenColumnFn] extends IMap2d[Column] {
	def getForRegion(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int): IMap2d[Column]
	def getForChunk(where: WChunk): IMap2d[Column] =
		getForRegion(where.xFrom, where.zFrom, where.xWidth, where.zWidth)
	def getForColumn(where: IPosColumn) = apply(where.xBlock, where.zBlock)
}
