package mod.iceandshadow3.compat.item

import net.minecraft.block.ComposterBlock
import net.minecraft.item.{ItemStack, PotionItem, UseAction}

object ItemQueries {
	def potion(is: ItemStack) = is.getItem.isInstanceOf[PotionItem]
	def food(is: ItemStack) = is.getItem.getUseAction(is) == UseAction.EAT
	def drink(is: ItemStack) = is.getItem.getUseAction(is) == UseAction.DRINK
	def shiny(is: ItemStack) = is.hasEffect
	def damageable(is: ItemStack) = is.isDamageable
	def damaged(is: ItemStack) = is.isDamaged
	def compostable(is: ItemStack) = ComposterBlock.field_220299_b.containsKey(is.getItem)
}
