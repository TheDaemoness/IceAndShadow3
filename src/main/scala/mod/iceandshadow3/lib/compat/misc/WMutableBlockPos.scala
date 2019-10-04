package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.lib.spatial.{IPosBlock, IPosColumn}
import net.minecraft.util.math.BlockPos

class WMutableBlockPos extends IPosBlock {
	private val mbp = new BlockPos.MutableBlockPos()
	def set(x: Int, y: Int, z: Int) = mbp.setPos(x, y, z)
	def set(col: IPosColumn, y: Int) = mbp.setPos(col.xBlock, y, col.zBlock)
	def set(block: IPosBlock) = mbp.setPos(block.xBlock, block.yBlock, block.zBlock)
	override def xBlock = mbp.getX
	override def yBlock = mbp.getY
	override def zBlock = mbp.getZ
	override def asBlockPos = mbp
}
