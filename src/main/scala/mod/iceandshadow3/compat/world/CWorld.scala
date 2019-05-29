package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.Vec3Conversions
import mod.iceandshadow3.spatial.{IPosColumn, IVec3}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{EnumDifficulty, World}

class CWorld(private[compat] val worldobj: World) extends TCWorld {
	override def exposeWorld(): World = worldobj
  override def world() = this

	def topSolid(where: IPosColumn): Option[IVec3] = {
		val chunk = worldobj.getChunk(where.xChunk, where.zChunk)
		for(yit <- 0 to 255) {
			val x = where.xBlock.toInt
			val y = 255-yit
			val z = where.zBlock.toInt
			if(chunk.getBlockState(x, y, z).isSolid) return Some(Vec3Conversions.fromBlockPos(new BlockPos(x, y, z)))
		}
		return None
	}
	def seed = worldobj.getWorldInfo.getSeed
	def isHardcore = worldobj.getWorldInfo.isHardcore
	def isPeaceful = worldobj.getWorldInfo.getDifficulty == EnumDifficulty.PEACEFUL
}
