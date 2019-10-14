package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.util.CNVCompat
import net.minecraft.item.BlockItemUseContext
import net.minecraft.util.math.BlockPos

trait TWUsageOnBlock {
	protected def posOn: BlockPos = expose.getPos
	protected[compat] def expose: BlockItemUseContext
	def yaw = expose.getPlacementYaw
	val block = new WBlockRef(expose.getWorld, posOn)
	val axis = CNVCompat.toOurAxis(expose.getFace.getAxis)
	val side = {
		val dir = expose.getFace
		block.atOffset(dir.getXOffset, dir.getYOffset, dir.getZOffset)
	}
	lazy val subhit = CNVCompat.fromVec3d(expose.getHitVec)
	lazy val canReplaceBlock = block.exposeBS().isReplaceable(expose)
	lazy val canReplaceSide = side.exposeBS().isReplaceable(expose)
}
