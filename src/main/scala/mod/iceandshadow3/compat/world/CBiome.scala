package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.TNamed
import net.minecraft.world.biome.Biome

//TODO: Manually-generated class stub.
class CBiome(private[world] val biome: Biome) extends TNamed {
  override protected[compat] def getNameTextComponent = biome.getDisplayName
  def temperature: Float = biome.getDefaultTemperature
  def downfall: Float = biome.getDownfall
  def isVeryHumid: Boolean = biome.isHighHumidity

  override def equals(b: Any): Boolean = b match {
    case cb: CBiome => cb.biome == this.biome
    case mb: Biome => mb == this.biome
    case _ => false
  }
}
