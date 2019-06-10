package mod.iceandshadow3.compat.dimension

import net.minecraft.world.dimension.DimensionType

/** Wrapper for DimensionType.
  */
case class WDimensionCoord(dimtype: DimensionType) {
  def isVanilla: Boolean = dimtype.isVanilla
  def getResourceLocation = DimensionType.getKey(dimtype)
  def getId: String = getResourceLocation.toString
}
object WDimensionCoord {
  val OVERWORLD = WDimensionCoord(DimensionType.OVERWORLD)
  val NETHER = WDimensionCoord(DimensionType.NETHER)
  val END = WDimensionCoord(DimensionType.THE_END)
}
