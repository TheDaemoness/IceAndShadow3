package mod.iceandshadow3.compat.world

import net.minecraft.world.dimension.DimensionType

/** Wrapper for DimensionType.
  */
case class CDimensionCoord(dimtype: DimensionType) {
  def isVanilla: Boolean = dimtype.isVanilla
  def getResourceLocation = DimensionType.func_212678_a(dimtype)
  def getId: String = getResourceLocation.toString
}
object CDimensionCoord {
  val OVERWORLD = CDimensionCoord(DimensionType.OVERWORLD)
  val NETHER = CDimensionCoord(DimensionType.NETHER)
  val END = CDimensionCoord(DimensionType.THE_END)
}
