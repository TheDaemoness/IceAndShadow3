package mod.iceandshadow3.lib.compat.item

import net.minecraft.block.ComposterBlock
import net.minecraft.enchantment.{EnchantmentHelper, Enchantments}
import net.minecraft.item.{PotionItem, UseAction}

object ItemQueries {
	def potion(item: WItem) = item.asItem().isInstanceOf[PotionItem]
	def ingestable(item: WItem) = {
		val action = item.asItem().getUseAction(item.asWItemStack().asItemStack())
		action == UseAction.EAT || action == UseAction.DRINK
	}
	def shiny(is: WItemStack) = is.asItemStack().hasEffect
	def damageable(is: WItemStack) = is.asItemStack().isDamageable
	def damaged(is: WItemStack) = is.asItemStack().isDamaged
	def compostable(item: WItem) = ComposterBlock.CHANCES.containsKey(item.asItem())
	def silktouch(is: WItemStack) = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, is.asItemStack()) > 0
}
