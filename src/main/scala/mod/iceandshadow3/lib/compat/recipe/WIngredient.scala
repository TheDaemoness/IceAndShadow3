package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.base.TLogicWithItem
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemType}
import net.minecraft.item.crafting.Ingredient

case class WIngredient private[compat](expose: Ingredient)

object WIngredient {
	def apply(what: Ingredient): WIngredient = new WIngredient(what)
	implicit def apply(what: TLogicWithItem): WIngredient = apply(what.toWItemType)
	implicit def apply(what: WItemType): WIngredient = WIngredient(Ingredient.fromItems(what))
	implicit def apply(what: WItemStack): WIngredient = WIngredient(Ingredient.fromStacks(what.asItemStack()))
	def fromStacks(what: WItemStack*): WIngredient =
		WIngredient(Ingredient.fromStacks(what.map(_.asItemStack()):_*))
	def fromItems(what: WItemType*): WIngredient =
		WIngredient(Ingredient.fromItems(what:_*))
	val empty = apply(Ingredient.EMPTY)
}
