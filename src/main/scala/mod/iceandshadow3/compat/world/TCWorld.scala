package mod.iceandshadow3.compat.world

import net.minecraft.world.World

/** Base trait for world references.
  * Written under the realization that under current design, several other references can also function as world references.
  * This trait includes most of the stuff that's more generally useful to IaS3.
  */
trait TCWorld {
  protected[compat] def exposeWorld(): World
  def world() = new CWorld(exposeWorld())

  def isServerSide: Boolean = !exposeWorld().isRemote
  def isClientSide: Boolean = exposeWorld().isRemote
  def rng(): java.util.Random = exposeWorld().rand
  def dimension = new CDimension(exposeWorld().dimension)
  def dimensionCoord = CDimensionCoord(exposeWorld().dimension.getType)
  def gameTime: Long = exposeWorld().getGameTime
}
