package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.lib.spatial.IPosBlock
import net.minecraft.util.math.BlockPos

class WMutableBlockPos extends IPosBlock {
	private val mbp = new BlockPos.MutableBlockPos()
	def set(x: Int, y: Int, z: Int) = mbp.setPos(x, y, z)
	override def yBlock = mbp.getY
	override def xBlock = mbp.getX
	override def zBlock = mbp.getZ
	override def asBlockPos = mbp
}
