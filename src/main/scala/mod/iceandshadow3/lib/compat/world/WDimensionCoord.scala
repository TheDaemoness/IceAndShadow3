package mod.iceandshadow3.lib.compat.world

import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType

/** Wrapper for DimensionType.
  */
case class WDimensionCoord(dimtype: DimensionType) {
  def isVanilla: Boolean = dimtype.isVanilla
  def getResourceLocation = DimensionType.getKey(dimtype)
  def getId: String = getResourceLocation.toString
  def worldIs(world: TWWorld) = world.dimensionCoord == this
  def worldIs(world: World) = world.dimension.getType == this.dimtype
  def worldIs(entity: Entity) = entity.dimension  == this.dimtype
}
object WDimensionCoord {
  val OVERWORLD = WDimensionCoord(DimensionType.field_223227_a_)
  val NETHER = WDimensionCoord(DimensionType.field_223228_b_)
  val END = WDimensionCoord(DimensionType.field_223229_c_)
}
