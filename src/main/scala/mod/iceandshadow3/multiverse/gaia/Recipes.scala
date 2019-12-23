package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.item.{WInventoryCrafting, WItemStack}
import mod.iceandshadow3.lib.compat.recipe.ECraftingType
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.item.LogicCrafting

object Recipes {
	import mod.iceandshadow3.multiverse.DomainGaia._
	Registrar.addRecipeCallback("dynamic.grow_with_minerals", s => ECraftingType.CRAFT_SPECIAL(new LogicCrafting(s) {
		override def fitsIn(width: Int, height: Int) = width*height >= 2
		override def matches(what: WInventoryCrafting, world: WWorld): Boolean = {
			var foundMinerals = false
			var foundGrowable = false
			for(item <- what) {
				if(!foundMinerals && item.matches(Items.minerals)) foundMinerals = true
				else if(!foundGrowable && item.toBlockState.fold(false)(LIMinerals.canGrow)) foundGrowable = true
				else if(!item.isEmpty) return false
			}
			foundGrowable && foundMinerals
		}
		override def apply(what: WInventoryCrafting): WItemStack = {
			for(item <- what) {
				if(!item.isEmpty && !item.matches(Items.minerals)) return item.copy.setCount(2)
			}
			WItemStack.empty
		}
	}))
}
