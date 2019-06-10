package mod.iceandshadow3.gen

import mod.iceandshadow3.compat.block.`type`.{BBlockType, BlockTypeSimple}

abstract class BChunkSource(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) {
	def getColumn(x: Int, z: Int): Array[BBlockType]
	def getDefaultAir(y: Int): BBlockType = BlockTypeSimple.AIR
}
