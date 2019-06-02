package mod.iceandshadow3.gen

import mod.iceandshadow3.compat.block.BBlockType

abstract class BChunkSource(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) {
	def getBlock(x: Int, y: Int, z: Int): BBlockType
}
