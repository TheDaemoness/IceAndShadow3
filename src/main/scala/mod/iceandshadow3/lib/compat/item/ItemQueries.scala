package mod.iceandshadow3.lib.compat.item

import net.minecraft.block.ComposterBlock
import net.minecraft.enchantment.{EnchantmentHelper, Enchantments}
import net.minecraft.item.{PotionItem, UseAction}

object ItemQueries {
	def potion(item: WItem) = item.asItem().isInstanceOf[PotionItem]
	def ingestable(item: WItem) = {
		val action = item.asItem().getUseAction(item.asWItemStack().expose())
		action == UseAction.EAT || action == UseAction.DRINK
	}
	def shiny(is: WItemStack) = is.expose().hasEffect
	def damageable(is: WItemStack) = is.expose().isDamageable
	def damaged(is: WItemStack) = is.expose().isDamaged
	def compostable(item: WItem) = ComposterBlock.CHANCES.containsKey(item.asItem())
	def silktouch(is: WItemStack) = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, is.expose()) > 0
}
