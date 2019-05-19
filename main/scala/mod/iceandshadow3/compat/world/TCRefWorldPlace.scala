package mod.iceandshadow3.compat.world

import mod.iceandshadow3.util.IPositional
import mod.iceandshadow3.compat.Vec3Conversions._
import net.minecraft.world.EnumLightType

trait TCRefWorldPlace extends TCRefWorld {
  this: IPositional =>
  def light: Int = exposeWorld().getLight(position)
  def sunlight: Int = exposeWorld().getLightFor(EnumLightType.SKY, position)
  def blocklight: Int = exposeWorld().getLightFor(EnumLightType.BLOCK, position)
  //TODO: Confirm that getLightFor does what we need.

  def underSky: Boolean = exposeWorld().canSeeSky(position)
  def biome: CBiome = new CBiome(exposeWorld().getBiome(position))
}
