package mod.iceandshadow3.lib.spatial

object IPosChunk {
	val CHUNK_BITS: Long = 4
	val CHUNK_MULT: Long = 1L << CHUNK_BITS
	val CHUNK_MASK: Long = CHUNK_MULT - 1
}
trait IPosChunk {
	def xChunk: Int
	def zChunk: Int
}

trait IPosColumn extends IPosChunk {
	import IPosChunk._
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
