package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.util.TLocalized
import net.minecraft.world.biome.Biome

//TODO: Manually-generated class stub.
class WBiome(private[world] val biome: Biome) extends TLocalized {
  override protected[compat] def getLocalizedName = biome.getDisplayName
  def temperature: Float = biome.getDefaultTemperature
  def downfall: Float = biome.getDownfall
  def isVeryHumid: Boolean = biome.isHighHumidity

  override def equals(b: Any): Boolean = b match {
    case cb: WBiome => cb.biome == this.biome
    case mb: Biome => mb == this.biome
    case _ => false
  }
}
