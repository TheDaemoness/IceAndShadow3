package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.util.Vec3
import net.minecraft.world.dimension.Dimension

/** Wrapper for Dimension.
*/
class CDimension(private[compat] val dim: Dimension) extends TCWorld {
	def canRespawnHere = dim.canRespawnHere

	def getWorldSpawn: Vec3 = if(canRespawnHere) dim.getWorld.getSpawnPoint else null
  def getCoord: CDimensionCoord = CDimensionCoord(dim.getType)
  def isVanilla: Boolean = dim.getType.isVanilla //Convenience method.
  override protected def exposeWorld() = dim.getWorld
}
