package mod.iceandshadow3.gen

import mod.iceandshadow3.lib.compat.block.`type`.{CommonBlockTypes, TBlockStateSource}

abstract class BChunkSource(val xFrom: Int, val zFrom: Int, val xWidth: Int, val zWidth: Int) {
	def getColumn(x: Int, z: Int): Array[TBlockStateSource]
	/** Called to fill any null entries in the returned array.*/
	def getDefault(y: Int): TBlockStateSource = CommonBlockTypes.AIR
}
