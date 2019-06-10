package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.dimension.{WDimension, WDimensionCoord}
import net.minecraft.world.{IWorld, World}

/** Base trait for world references.
  * Written under the realization that under current design, several other references can also function as world references.
  * This trait includes most of the stuff that's more generally useful to IaS3.
  */
trait TWWorld {
  protected[compat] def exposeWorld(): IWorld
  def world() = new WWorld(exposeWorld())

  def isServerSide: Boolean = !exposeWorld().isRemote
  def isClientSide: Boolean = exposeWorld().isRemote
  def rng(): java.util.Random = exposeWorld().getRandom
  def rng(base: Int, variance: Int): Int = base+rng().nextInt(variance+1)
  def rng(base: Float, variance: Float): Float = base+rng().nextFloat()*variance
  def dimension = new WDimension(exposeWorld().getDimension)
  def dimensionCoord = WDimensionCoord(exposeWorld().getDimension.getType)
  def gameTime: Long = exposeWorld().getWorldInfo.getGameTime
}
