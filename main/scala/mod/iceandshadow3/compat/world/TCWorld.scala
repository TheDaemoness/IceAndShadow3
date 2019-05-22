package mod.iceandshadow3.compat.world

import net.minecraft.world.World

/** Base trait for world references.
  * Written under the realization that under current design, several other references can also function as world references.
  * After all, block data is often sent alongside a world.
  */
trait TCWorld {
  protected def exposeWorld(): World

  def isServerSide: Boolean = !exposeWorld().isRemote
  def isClientSide: Boolean = exposeWorld().isRemote
  def rng(): java.util.Random = exposeWorld().rand
  def dimension = new CDimension(exposeWorld().dimension)
  def dimensionCoord = new CDimensionCoord(exposeWorld().dimension.getType)
}
