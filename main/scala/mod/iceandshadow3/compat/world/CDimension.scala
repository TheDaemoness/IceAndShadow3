package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.util.Vec3
import net.minecraft.world.dimension.Dimension

//TODO: Manually-generated class stub.
class CDimension(private[compat] val dim: Dimension) {
  def getWorldSpawn: Vec3 = if(dim.canRespawnHere) dim.getSpawnCoordinate else null
  def isVanilla: Boolean = dim.getType.isVanilla
}
