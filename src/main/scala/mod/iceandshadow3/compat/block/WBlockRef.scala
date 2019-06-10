package mod.iceandshadow3.compat.block

import mod.iceandshadow3.compat.CNVVec3
import mod.iceandshadow3.compat.block.`type`.BBlockType
import mod.iceandshadow3.compat.world.TWWorldPlace
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.chunk.IChunk

class WBlockRef(chunk: IChunk, pos: BlockPos, bs: BlockState) extends WBlockView(chunk.getWorldForge, pos, bs)
	with TWWorldPlace
{
	override protected def acquireBS() = chunk.getBlockState(pos)
	override protected[compat] def exposeWorld(): IWorld = chunk.getWorldForge

	def this(w: IWorld, v: IVec3) {
		this(w.getChunk(v.xChunk, v.zChunk), CNVVec3.toBlockPos(v), null)
	}

	def this(w: IWorld, p: BlockPos) = {
		this(w.getChunk(p.getX >> 4, p.getZ >> 4), p, null)
	}

	def this(w: IWorld, p: BlockPos, bs: BlockState) = {
		this(w.getChunk(p.getX >> 4, p.getZ >> 4), p, bs)
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
