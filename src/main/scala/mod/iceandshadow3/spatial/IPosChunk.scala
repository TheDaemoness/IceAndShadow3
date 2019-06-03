package mod.iceandshadow3.spatial

object IPosChunk {
	val CHUNK_BITS: Long = 4
	val CHUNK_MULT: Long = 1L << CHUNK_BITS
	val CHUNK_MASK: Long = CHUNK_MULT - 1
}
trait IPosChunk {
	def xChunk: Int
	def zChunk: Int
}
