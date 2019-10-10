package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.util.CNVCompat._
import mod.iceandshadow3.lib.spatial.IPositionalCoarse
import net.minecraft.world.LightType

trait TWWorldPlace extends TWWorld {
  this: IPositionalCoarse =>
  def light: Int = exposeWorld().getLight(posCoarse)
  def sunlight: Int = exposeWorld().getLightFor(LightType.SKY, posCoarse)
  def blocklight: Int = exposeWorld().getLightFor(LightType.BLOCK, posCoarse)

  def underSky: Boolean = exposeWorld().canBlockSeeSky(posCoarse)
  def biome: WBiome = new WBiome(exposeWorld().getBiome(posCoarse))

  def playSound(sound: WSound, volume: Float = 1f, freqshift: Float = 1f): Unit =
    sound.play(this, this.posCoarse, volume, freqshift)

  def getShadowPresence: Float = {
    1f-Math.max(sunlight, blocklight)/15f
  }
}
