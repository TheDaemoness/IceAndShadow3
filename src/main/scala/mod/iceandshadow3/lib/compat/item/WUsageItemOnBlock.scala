package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.LogicPair
import mod.iceandshadow3.lib.compat.misc.TWUsageOnBlock
import net.minecraft.item.{BlockItemUseContext, ItemUseContext}
import net.minecraft.util.math.BlockPos

class WUsageItemOnBlock(
	lp: LogicPair[BLogicItem],
	final override protected val biuc: BlockItemUseContext,
	final override protected val posOn: BlockPos
)
extends WUsageItem(lp, biuc.getItem, biuc.getPlayer, biuc.getHand, biuc.isPlacerSneaking)
with TWUsageOnBlock {
	def this(lp: LogicPair[BLogicItem], c: ItemUseContext) = this(lp, new BlockItemUseContext(c), c.getPos)
}
