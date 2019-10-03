package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.block.{WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.spatial.IPosBlock
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.IChunk

class WChunk protected(protected val chunk: IChunk, pos: ChunkPos) extends BWorldRegionRef(
	pos.getXStart,
	pos.getZStart,
	pos.getXEnd+1,
	pos.getZEnd+1,
) {
	def this(chunk: IChunk) = this(chunk, chunk.getPos)
	override def apply(where: IPosBlock) = {
		val bp = where.toBlockPos
		new WBlockRef(chunk, bp, chunk.getBlockState(bp))
	}

	override def state(where: IPosBlock) =
		new WBlockState(chunk.getBlockState(where.asBlockPos))

	override def update(where: IPosBlock, newtype: TBlockStateSource): Unit =
		chunk.setBlockState(where.asBlockPos, newtype.exposeBS(), false)
}
