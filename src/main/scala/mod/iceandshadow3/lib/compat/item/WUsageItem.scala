package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.LogicPair
import mod.iceandshadow3.lib.compat.misc.WUsage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand

class WUsageItem(
	lp: LogicPair[BLogicItem],
	is: ItemStack,
	p: PlayerEntity,
	hand: Hand,
	sneaking: Boolean
)
extends WUsage(is, p, hand, sneaking)
