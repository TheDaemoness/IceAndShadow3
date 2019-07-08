package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.IaS3
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType

/** Wrapper for DimensionType.
  */
case class WDimensionCoord(dimtype: DimensionType) {
  def isVanilla: Boolean = dimtype.isVanilla
  def resourceLocation = DimensionType.getKey(dimtype)
  def getId: String = resourceLocation.toString
  def worldIs(world: TWWorld) = world.dimensionCoord == this
  def worldIs(world: World) = world.dimension.getType == this.dimtype
  def worldIs(entity: Entity) = entity.dimension  == this.dimtype
}
object WDimensionCoord {
  def isVoid(what: WDimensionCoord) = what == null || what == VOID
  val VOID = new WDimensionCoord(null) {
    override val isVanilla = false
    override val resourceLocation = new ResourceLocation("", "")
  }
  val OVERWORLD = WDimensionCoord(DimensionType.field_223227_a_)
  val NETHER = WDimensionCoord(DimensionType.field_223228_b_)
  val END = WDimensionCoord(DimensionType.field_223229_c_)
}
