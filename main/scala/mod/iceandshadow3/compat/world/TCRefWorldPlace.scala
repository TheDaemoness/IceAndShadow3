package mod.iceandshadow3.compat.world

import mod.iceandshadow3.util.IPositional
import mod.iceandshadow3.compat.Vec3Conversions._
import net.minecraft.world.EnumLightType

trait TCRefWorldPlace extends TCRefWorld {
  this: IPositional =>
  def light: Int = getWorld().getLight(position)
  def sunlight: Int = getWorld().getLightFor(EnumLightType.SKY, position)
  def blocklight: Int = getWorld().getLightFor(EnumLightType.BLOCK, position)
  //TODO: Confirm that getLightFor does what we need.

  def underSky: Boolean = getWorld().canSeeSky(position)
  def biome: CBiome = new CBiome(getWorld().getBiome(position))
}
