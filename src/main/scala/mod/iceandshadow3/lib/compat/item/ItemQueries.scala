package mod.iceandshadow3.lib.compat.item

import net.minecraft.block.ComposterBlock
import net.minecraft.item.{PotionItem, UseAction}

object ItemQueries {
	def potion(item: BWItem) = item.asItem().isInstanceOf[PotionItem]
	def ingestable(item: BWItem) = {
		val action = item.asItem().getUseAction(item.asWItemStack().asItemStack())
		action == UseAction.EAT || action == UseAction.DRINK
	}
	def shiny(is: WItemStack) = is.asItemStack().hasEffect
	def damageable(is: WItemStack) = is.asItemStack().isDamageable
	def damaged(is: WItemStack) = is.asItemStack().isDamaged
	def compostable(item: BWItem) = ComposterBlock.CHANCES.containsKey(item.asItem())
}
