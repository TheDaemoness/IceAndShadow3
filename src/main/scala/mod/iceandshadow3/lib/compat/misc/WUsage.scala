package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand

class WUsage(
	is: ItemStack,
	p: PlayerEntity,
	hand: Hand,
	val sneaking: Boolean
) {
	val stack = new WItemStackOwned(is, CNVEntity.wrap(p))
	val mainhand = hand == Hand.MAIN_HAND
}
