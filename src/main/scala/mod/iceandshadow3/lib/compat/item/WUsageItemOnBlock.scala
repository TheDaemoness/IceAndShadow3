package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.misc.TWUsageOnBlock
import net.minecraft.item.{BlockItemUseContext, ItemUseContext}
import net.minecraft.util.math.BlockPos

class WUsageItemOnBlock(
	logic: BLogicItem,
	final override protected[compat] val expose: BlockItemUseContext,
	final override protected val posOn: BlockPos
)
extends WUsageItem(logic, expose.getItem, expose.getPlayer, expose.getHand, expose.isPlacerSneaking)
with TWUsageOnBlock {
	def this(logic: BLogicItem, c: ItemUseContext) = this(logic, new BlockItemUseContext(c), c.getPos)
}
