package mod.iceandshadow3.lib.spatial

import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.world.TWWorld
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
	def withOffset(what: IPosColumn, x: Int, z: Int) = wrap(what.xBlock, what.zBlock)
	def lookupFrom[T](what: IMap2d[T], coord: IPosColumn, xOffset: Int = 0, zOffset: Int = 0) =
		what(coord.xBlock + xOffset, coord.zBlock + zOffset)
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
