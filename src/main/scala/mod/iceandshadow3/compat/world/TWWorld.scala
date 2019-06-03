package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.dimension.{WDimension, WDimensionCoord}
import net.minecraft.world.World

/** Base trait for world references.
  * Written under the realization that under current design, several other references can also function as world references.
  * This trait includes most of the stuff that's more generally useful to IaS3.
  */
trait TWWorld {
  protected[compat] def exposeWorld(): World
  def world() = new WWorld(exposeWorld())

  def isServerSide: Boolean = !exposeWorld().isRemote
  def isClientSide: Boolean = exposeWorld().isRemote
  def rng(): java.util.Random = exposeWorld().rand
  def dimension = new WDimension(exposeWorld().dimension)
  def dimensionCoord = WDimensionCoord(exposeWorld().dimension.getType)
  def gameTime: Long = exposeWorld().getGameTime
}
