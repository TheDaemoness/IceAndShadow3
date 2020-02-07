package mod.iceandshadow3.lib.compat.world

import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType

/** Wrapper for DimensionType such that it can be used as a "coordinate".
  */
case class WDimensionCoord(dimtype: DimensionType) {
  def isVanilla: Boolean = dimtype.isVanilla
  def resourceLocation = DimensionType.getKey(dimtype)
  def getId: String = resourceLocation.toString
  def unapply(world: TWWorld) = world.dimensionCoord == this
  def unapply(world: World) = world.dimension.getType == this.dimtype
  def unapply(entity: Entity) = entity.dimension == this.dimtype
}
object WDimensionCoord {
  def isVoid(what: WDimensionCoord) = what == null || what == VOID
  val VOID: WDimensionCoord = new WDimensionCoord(null) {
    override val isVanilla = false
    override val resourceLocation = new ResourceLocation("", "")
  }
  val OVERWORLD = WDimensionCoord(DimensionType.OVERWORLD)
  val NETHER = WDimensionCoord(DimensionType.THE_NETHER)
  val END = WDimensionCoord(DimensionType.THE_END)
}
