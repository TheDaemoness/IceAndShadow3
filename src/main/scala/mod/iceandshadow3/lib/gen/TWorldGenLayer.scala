package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.spatial.{BWorldRegion, IPosColumn}
import mod.iceandshadow3.lib.util.collect.{FixedMap2d, IMap2d}

trait TWorldGenLayer[Fn <: BWorldGenColumnFn] extends IMap2d[Fn] {
	def getForRegion(where: BWorldRegion): IMap2d[Fn] = new FixedMap2d[Fn](
		where.xFrom, where.zFrom,
		where.xMax-where.xFrom+1,
		where.zMax-where.zFrom+1, apply
	)
	def getForChunk(where: WChunk) = getForRegion(where)
	def getForColumn(where: IPosColumn) = apply(where.xBlock, where.zBlock)
}
