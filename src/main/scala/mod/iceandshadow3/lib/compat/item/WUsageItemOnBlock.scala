package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.LogicPair
import mod.iceandshadow3.lib.compat.misc.TWUsageOnBlock
import net.minecraft.item.{BlockItemUseContext, ItemUseContext}

class WUsageItemOnBlock(lp: LogicPair[BLogicItem], c: ItemUseContext)
extends WUsageItem(lp, c.getItem, c.getPlayer, c.getHand, c.isPlacerSneaking) with TWUsageOnBlock {
	override protected val biuc = new BlockItemUseContext(c)
}
