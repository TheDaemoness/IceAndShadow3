package mod.iceandshadow3.compat.world

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.util.IPositional
import net.minecraft.world.EnumLightType

trait TCWorldPlace extends TCWorld {
  this: IPositional =>
  def light: Int = exposeWorld().getLight(position)
  def sunlight: Int = exposeWorld().getLightFor(EnumLightType.SKY, position)
  def blocklight: Int = exposeWorld().getLightFor(EnumLightType.BLOCK, position)
  //TODO: Confirm that getLightFor does what we need.

  def underSky: Boolean = exposeWorld().canSeeSky(position)
  def biome: CBiome = new CBiome(exposeWorld().getBiome(position))

  def playSound(sound: CSound, volume: Float, freqshift: Float): Unit =
    sound.play(this, this.position, volume, freqshift)
}
