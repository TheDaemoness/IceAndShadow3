package mod.iceandshadow3.compat.world

import net.minecraft.world.{EnumDifficulty, World}

class CWorld(private[compat] val worldobj: World) extends TCWorld {
	override def exposeWorld(): World = worldobj
  override def world() = this

	def seed = worldobj.getWorldInfo.getSeed
	def isHardcore = worldobj.getWorldInfo.isHardcore
	def isPeaceful = worldobj.getWorldInfo.getDifficulty == EnumDifficulty.PEACEFUL
}
