package mod.iceandshadow3.spatial

import SpatialConstants._

trait IPosChunk {
	def xChunk: Int
	def zChunk: Int
}

trait IPosColumn extends IPosChunk {
	def xBlock: Long
	def zBlock: Long
	def xChunk: Int = (xBlock >> CHUNK_BITS).toInt
	def zChunk: Int = (zBlock >> CHUNK_BITS).toInt
	def xSubChunk: Byte = (xBlock & CHUNK_MASK).toByte
	def zSubChunk: Byte = (zBlock & CHUNK_MASK).toByte
}

trait IPosBlock extends IPosColumn {
	def yBlock: Int
}