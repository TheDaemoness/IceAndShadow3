package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.util.CNVCompat._
import mod.iceandshadow3.lib.spatial.IPositionalCoarse
import net.minecraft.world.LightType

trait TWWorldPlace extends TWWorld {
  this: IPositionalCoarse =>
  def block: WBlockRef = new WBlockRef(exposeWorld(), posCoarse.asBlockPos)
  def light: Int = exposeWorld().getLight(posCoarse)
  def sunlight: Int = exposeWorld().func_226658_a_(LightType.SKY, posCoarse)
  def blocklight: Int = exposeWorld().func_226658_a_(LightType.BLOCK, posCoarse)

  def underSky: Boolean = exposeWorld().canBlockSeeSky(posCoarse)
  def biome: WBiome = new WBiome(exposeWorld().func_226691_t_(posCoarse))

  def playSound(sound: WSound, volume: Float = 1f, freqshift: Float = 1f): Unit =
    sound.play(this, this.posCoarse, volume, freqshift)

  def getShadowPresence: Float = {
    1f-Math.max(sunlight, blocklight)/15f
  }
}
