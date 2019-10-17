package mod.iceandshadow3.lib.spatial

import mod.iceandshadow3.lib.util.collect.IMap2d
import net.minecraft.util.math.BlockPos

object IPosChunk {
	val CHUNK_BITS: Int = 4
	val CHUNK_MULT: Long = 1L << CHUNK_BITS
	val CHUNK_MASK: Long = CHUNK_MULT - 1
}
trait IPosChunk {
	def xChunk: Int
	def zChunk: Int
}

object IPosColumn {
	def wrap(x: Int, z: Int) = new IPosColumn {
		override def xBlock = x
		override def zBlock = z
	}
}
trait IPosColumn extends IPosChunk {
	import IPosChunk._
	def xBlock: Int
	def zBlock: Int
	def xChunk: Int = xBlock >> CHUNK_BITS
	def zChunk: Int = zBlock >> CHUNK_BITS
	def xSubChunk: Byte = (xBlock & CHUNK_MASK).toByte
	def zSubChunk: Byte = (zBlock & CHUNK_MASK).toByte
}

trait IPosBlock extends IPosColumn {
	def yBlock: Int
	def toBlockPos: BlockPos = new BlockPos(xBlock, yBlock, zBlock)
	def asBlockPos: BlockPos = toBlockPos
}
