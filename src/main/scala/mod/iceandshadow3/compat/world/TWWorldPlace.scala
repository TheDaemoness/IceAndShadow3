package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.CNVVec3._
import mod.iceandshadow3.spatial.IPositional
import net.minecraft.world.EnumLightType

trait TWWorldPlace extends TWWorld {
  this: IPositional =>
  def light: Int = exposeWorld().getLight(position)
  def sunlight: Int = exposeWorld().getLightFor(EnumLightType.SKY, position)
  def blocklight: Int = exposeWorld().getLightFor(EnumLightType.BLOCK, position)
  //TODO: Confirm that getLightFor does what we need.

  def underSky: Boolean = exposeWorld().canSeeSky(position)
  def biome: WBiome = new WBiome(exposeWorld().getBiome(position))

  def playSound(sound: WSound, volume: Float, freqshift: Float): Unit =
    sound.play(this, this.position, volume, freqshift)
}
