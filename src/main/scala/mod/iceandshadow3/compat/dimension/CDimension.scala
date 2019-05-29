package mod.iceandshadow3.compat.dimension

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.compat.world.TCWorld
import mod.iceandshadow3.spatial.IVec3
import net.minecraft.world.dimension.Dimension

/** Wrapper for Dimension.
*/
class CDimension(private[compat] val dim: Dimension) extends TCWorld {
	def canRespawnHere = dim.canRespawnHere

	def getWorldSpawn: IVec3 = if(canRespawnHere) dim.getWorld.getSpawnPoint else null
  def getCoord: CDimensionCoord = CDimensionCoord(dim.getType)
  def isVanilla: Boolean = dim.getType.isVanilla //Convenience method.
  override protected[compat] def exposeWorld() = dim.getWorld
}
