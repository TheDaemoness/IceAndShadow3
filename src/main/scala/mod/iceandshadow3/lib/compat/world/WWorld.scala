package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.ServerAnalyses
import mod.iceandshadow3.lib.compat.block.{WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.compat.misc.ServerAnalyzer
import mod.iceandshadow3.lib.compat.util.CNVCompat
import mod.iceandshadow3.lib.spatial.{IPosBlock, IPosColumn, IVec3}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{Difficulty, IWorld}

class WWorld(private[compat] val worldobj: IWorld)
extends BWorldRegionRef(Int.MinValue, Int.MinValue, Int.MaxValue, Int.MaxValue)
with TWWorld {
	override def exposeWorld(): IWorld = worldobj
  override def world() = this

	def topSolid(where: IPosColumn): Option[IVec3] = {
		val chunk = worldobj.getChunk(where.xChunk, where.zChunk)
		val mbp = new BlockPos.MutableBlockPos
		for(yit <- 0 to 255) {
			val x = where.xBlock.toInt
			val y = 255-yit
			val z = where.zBlock.toInt
			mbp.setPos(x, y, z)
			if(chunk.getBlockState(mbp).isSolid) return Some(CNVCompat.fromBlockPos(new BlockPos(x, y, z)))
		}
		None
	}
	def seed = worldobj.getWorldInfo.getSeed
	def isHardcore = worldobj.getWorldInfo.isHardcore
	def isPeaceful = worldobj.getWorldInfo.getDifficulty == Difficulty.PEACEFUL

	def apply[In, Out](what: ServerAnalyzer[In, Out]): Option[In => Out] = {
		val serverNullable = worldobj.getWorld.getServer
		if(serverNullable == null) None else Some(ServerAnalyses(serverNullable)(what))
	}
	def apply[In, Out](what: ServerAnalyzer[In, Out], arg: In): Option[Out] =
		apply(what).fold[Option[Out]](None)(fn => Some(fn(arg)))

	override def apply(where: IPosBlock) =
		new WBlockRef(worldobj, where.toBlockPos)
	override def state(where: IPosBlock) = new WBlockState(worldobj.getBlockState(where.asBlockPos))
	override def update(where: IPosBlock, newtype: TBlockStateSource): Unit =
		worldobj.setBlockState(where.asBlockPos, newtype.exposeBS(), 0x3)
}
