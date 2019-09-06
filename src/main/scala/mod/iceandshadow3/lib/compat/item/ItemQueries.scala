package mod.iceandshadow3.lib.compat.item

import net.minecraft.block.ComposterBlock
import net.minecraft.item.{PotionItem, UseAction}

object ItemQueries {
	def potion(item: BWItem) = item.exposeItem().isInstanceOf[PotionItem]
	def ingestable(item: BWItem) = {
		val action = item.exposeItem().getUseAction(item.asWItemStack().exposeItems())
		action == UseAction.EAT || action == UseAction.DRINK
	}
	def shiny(is: WItemStack) = is.exposeItems().hasEffect
	def damageable(is: WItemStack) = is.exposeItems().isDamageable
	def damaged(is: WItemStack) = is.exposeItems().isDamaged
	def compostable(item: BWItem) = ComposterBlock.CHANCES.containsKey(item.exposeItem())
}
