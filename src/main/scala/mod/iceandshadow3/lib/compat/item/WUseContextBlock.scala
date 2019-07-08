package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.util.CNVCompat
import mod.iceandshadow3.lib.util.LogicPair
import net.minecraft.item.{BlockItemUseContext, ItemUseContext}

class WUseContextBlock(lp: LogicPair[BLogicItem], c: ItemUseContext)
extends WUseContext(lp, c.getItem, c.getPlayer, c.getHand, c.isPlacerSneaking) {
	private val biuc = new BlockItemUseContext(c)
	def yaw = c.getPlacementYaw
	val block = new WBlockRef(c.getWorld, c.getPos)
	val side = {
		val dir = c.getFace
		block.atOffset(dir.getXOffset, dir.getYOffset, dir.getZOffset)
	}
	lazy val subhit = CNVCompat.fromVec3d(c.getHitVec)
	lazy val canReplaceBlock = block.exposeBS().isReplaceable(biuc)
	lazy val canReplaceSide = side.exposeBS().isReplaceable(biuc)
}
