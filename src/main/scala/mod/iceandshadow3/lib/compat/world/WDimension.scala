package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.util.CNVCompat._
import mod.iceandshadow3.lib.spatial.IVec3
import net.minecraft.world.dimension.Dimension

/** Wrapper for Dimension.
*/
class WDimension(private[compat] val dim: Dimension) extends TWWorld {
	def canRespawnHere = dim.canRespawnHere

	def getWorldSpawn: IVec3 = if(canRespawnHere) dim.getWorld.getSpawnPoint else null
  def getCoord: WDimensionCoord = WDimensionCoord(dim.getType)
  def isVanilla: Boolean = dim.getType.isVanilla //Convenience method.
	def isHellish = dim.doesWaterVaporize
  override protected[compat] def exposeWorld() = dim.getWorld
}
