package mod.iceandshadow3.compat.block

import mod.iceandshadow3.compat.CNVVec3
import mod.iceandshadow3.compat.block.`type`.BBlockType
import mod.iceandshadow3.compat.world.TWWorldPlace
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.chunk.IChunk

class WBlockRef(chunk: IChunk, pos: BlockPos, bs: IBlockState) extends WBlockView(chunk.getWorldForge, pos, bs)
	with TWWorldPlace
{
	override protected def acquireBS() = chunk.getBlockState(pos)
	override protected[compat] def exposeWorld(): IWorld = chunk.getWorldForge

	def this(w: IWorld, v: IVec3) {
		this(w.getChunk(v.xChunk, v.zChunk), CNVVec3.toBlockPos(v), null)
	}

	def this(w: IWorld, p: BlockPos) = {
		this(w.getChunkDefault(p), p, null)
	}

	def this(w: IWorld, p: BlockPos, bs: IBlockState) = {
		this(w.getChunkDefault(p), p, bs)
	}

	override def atOffset(x: Int, y: Int, z: Int): WBlockRef =
		new WBlockRef(exposeWorld(), pos.add(x, y, z))

	def set(what: BBlockType): Unit = {
		if(isServerSide) {
			chunk.setBlockState(pos, what.state(), false)
			refresh()
		}
	}
	def break(drops: Boolean): Unit = {
		//TODO: Block type on breakage.
		if(isServerSide) {
			exposeWorld().destroyBlock(pos, drops)
			refresh()
		}
	}
	def break(ifNoHarderThan: Float, drops: Boolean): Boolean = {
		if(getHardness <= ifNoHarderThan) {break(drops); true}
		else false
	}
}
