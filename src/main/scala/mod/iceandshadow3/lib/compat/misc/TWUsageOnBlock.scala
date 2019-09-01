package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.util.CNVCompat
import net.minecraft.item.BlockItemUseContext
import net.minecraft.util.math.BlockPos

trait TWUsageOnBlock {
	protected def posOn: BlockPos = biuc.getPos
	protected def biuc: BlockItemUseContext
	def yaw = biuc.getPlacementYaw
	val block = new WBlockRef(biuc.getWorld, posOn)
	val axis = CNVCompat.toOurAxis(biuc.getFace.getAxis)
	val side = {
		val dir = biuc.getFace
		block.atOffset(dir.getXOffset, dir.getYOffset, dir.getZOffset)
	}
	lazy val subhit = CNVCompat.fromVec3d(biuc.getHitVec)
	lazy val canReplaceBlock = block.exposeBS().isReplaceable(biuc)
	lazy val canReplaceSide = side.exposeBS().isReplaceable(biuc)
}
