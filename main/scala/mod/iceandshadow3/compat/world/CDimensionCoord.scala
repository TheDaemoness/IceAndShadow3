package mod.iceandshadow3.compat.world

import mod.iceandshadow3.data.{DatumString, IDataTreeRW}
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.DimensionManager

/** Wrapper for DimensionType.
  */
case class CDimensionCoord(dimtype: DimensionType) {
  def isVanilla: Boolean = dimtype.isVanilla
  def getId: String = DimensionType.func_212678_a(dimtype).toString
}
object CDimensionCoord {
  val OVERWORLD = new CDimensionCoord(DimensionType.OVERWORLD)
  val NETHER = new CDimensionCoord(DimensionType.NETHER)
  val END = new CDimensionCoord(DimensionType.THE_END)
}
