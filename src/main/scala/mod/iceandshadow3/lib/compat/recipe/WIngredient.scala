package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemType}
import net.minecraft.item.crafting.Ingredient

case class WIngredient private[compat](expose: Ingredient)

object WIngredient {
	def apply(what: Ingredient): WIngredient = new WIngredient(what)
	implicit def fromStacks(what: WItemStack*): WIngredient =
		WIngredient(Ingredient.fromStacks(what.map(_.asItemStack()):_*))
	implicit def fromItems(what: WItemType*): WIngredient =
		WIngredient(Ingredient.fromItems(what:_*))
	val empty = apply(Ingredient.EMPTY)
}
