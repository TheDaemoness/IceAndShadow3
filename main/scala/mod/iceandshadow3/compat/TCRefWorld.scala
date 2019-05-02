package mod.iceandshadow3.compat

import net.minecraft.world.World

/** Base trait for world references.
  * Written under the realization that under current design, several other references can also function as world references.
  * After all, block data is often sent alongside a world.
  */
trait TCRefWorld {
  protected def getWorld(): World

  def isServerSide(): Boolean = !getWorld.isRemote

}
