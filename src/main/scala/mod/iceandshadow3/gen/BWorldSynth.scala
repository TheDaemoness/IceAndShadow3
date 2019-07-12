package mod.iceandshadow3.gen

import mod.iceandshadow3.lib.compat.block.`type`.{BBlockType, BlockTypeSimple}
import mod.iceandshadow3.lib.compat.world.BRegionRef
import mod.iceandshadow3.util.collect.IMap2d

abstract class BWorldSynth[
	ColumnDataType,
	WorldDataType <: BWorldGenData[ColumnDataType]
] (protected val data: WorldDataType) {
	final def write(chunk: BRegionRef): Unit = {
		// WARNING: FRAGILE. Assumes that the "chunks" passed in will NOT cross RegionData boundaries.
		val region = data.region(chunk.xFrom, chunk.zFrom)
		for(xi <- chunk.xFrom to chunk.xMax) {
			for(zi <- chunk.zFrom to chunk.zMax) {
				val column = region.apply(xi, zi)
				val fn = getSynth(xi, zi, column)
				for(yi <- 0 to 255) {
					val terrain = fn(column, yi)
					 chunk(xi, yi, zi) = if(terrain != null) terrain else getDefault(yi)
				}
			}
		}
	}
	protected def getSynth(xBlock: Int, zBlock: Int, genData: ColumnDataType): (ColumnDataType, Int) => BBlockType
	def getDefault(y: Int): BBlockType = BlockTypeSimple.AIR
}
