package mod.iceandshadow3.compat

import net.minecraft.world.World

//remove if not needed
import scala.collection.JavaConversions._

/**
  * Base class for world references.
  * Written under the realization that under current design, block references can also function as world references.
  * After all, block data is often sent alongside a world.
  */
trait TCRefWorld {
  protected def getWorld(): World

  def isServerSide(): Boolean = !getWorld.isRemote

  def getDimension(): CDimension =
    CDimension.get(getWorld.provider.getDimension)
}