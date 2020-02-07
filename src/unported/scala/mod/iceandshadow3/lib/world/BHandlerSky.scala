package mod.iceandshadow3.lib.world

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.world.{EMoonPhase, WWorld}
import mod.iceandshadow3.lib.util.Color

abstract class BHandlerSky {
	def hasLuma: Boolean
	def isDay(world: WWorld): Boolean = hasLuma
	//def luma(world: WWorld, partialTicks: Float): Float = if(hasLuma) 15f else 0f
	def angle(world: WWorld, worldTime: Long, partialTicks: Float): Float
	//def colorDefault: Color
	//Nullable def colorDynamic(world: WWorld, xBlock: Int, yBlock: Int, zBlock: Int, partialTicks: Float): Color = null
	//def stars(world: WWorld, partialTicks: Float): Float = 0f
	def moon(world: WWorld, ticks: Long): EMoonPhase = EMoonPhase.NEW
}


