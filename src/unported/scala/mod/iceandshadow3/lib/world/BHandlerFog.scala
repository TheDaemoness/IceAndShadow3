package mod.iceandshadow3.lib.world

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.util.Color

abstract class BHandlerFog {
	def hasFogAt(world: WWorld, xBlock: Int, zBlock: Int): Boolean = true
	def colorDefault: Color
	@Nullable def colorDynamic(world: WWorld, skyAngle: Float, partialTicks: Float): Color = null
}
object BHandlerFog {
	val none = new BHandlerFog {
		override def hasFogAt(world: WWorld, xBlock: Int, zBlock: Int) = false
		override def colorDefault = Color.BLACK
	}
	val black = new BHandlerFog {
		override def colorDefault = Color.BLACK
	}
}
