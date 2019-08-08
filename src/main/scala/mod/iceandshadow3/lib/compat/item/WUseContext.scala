package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.LogicPair
import mod.iceandshadow3.lib.compat.entity.{CNVEntity, WEntityPlayer}
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand

class WUseContext(
	lp: LogicPair[BLogicItem],
	is: ItemStack,
	p: PlayerEntity,
	hand: Hand,
	val sneaking: Boolean
) {
	val stack = new WItemStack(is, p)
	val user = CNVEntity.wrap(p)
	val mainhand = hand == Hand.MAIN_HAND
	lazy val state = stack.exposeStateData(lp)
}
