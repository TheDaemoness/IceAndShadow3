package mod.iceandshadow3.compat.block

import mod.iceandshadow3.compat.CNVVec3
import mod.iceandshadow3.compat.world.TWWorldPlace
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk

class WBlockRef(chunk: Chunk, pos: BlockPos, bs: IBlockState) extends WBlockView(chunk, pos, bs)
	with TWWorldPlace
{
	override protected def acquireBS() = chunk.getBlockState(pos)
	override protected[compat] def exposeWorld(): World = chunk.getWorldForge

	def this(w: World, v: IVec3) {
		this(w.getChunk(v.xChunk, v.zChunk), CNVVec3.toBlockPos(v), null)
	}

	def this(w: World, p: BlockPos) = {
		this(w.getChunk(p), p, null)
	}

	def this(w: World, p: BlockPos, bs: IBlockState) = {
		this(w.getChunk(p), p, bs)
	}

	def getLuma: Int = exposeBS().getLightValue(exposeWorld(), pos)

	override def atOffset(x: Int, y: Int, z: Int): WBlockRef =
		new WBlockRef(exposeWorld(), pos.add(x, y, z))

	def break(drops: Boolean): Unit = {
		//TODO: Block type on breakage.
		if(isServerSide) exposeWorld().destroyBlock(pos, drops)
	}
	def break(ifNoHarderThan: Float, drops: Boolean): Boolean = {
		if(getHardness <= ifNoHarderThan) {break(drops); true}
		else false
	}
}
