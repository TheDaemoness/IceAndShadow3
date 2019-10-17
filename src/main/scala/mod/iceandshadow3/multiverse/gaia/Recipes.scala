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
				if(!item.isEmpty && !item.matches(Items.minerals)) return item.copy.changeCount(2)
			}
			WItemStack.empty
		}
	}))
	for(variant <- ELivingstoneTypes.values()) {
		val id = variant.ordinal()
		val stonename = s"gaia_livingstone_${variant.name}"
		val shalename = s"gaia_livingshale_${variant.name}"
		Registrar.addRecipeCallback(s"craft.$stonename.grow", name => {
			val minerals = Items.minerals.asWItem(0)
			ECraftingType.CRAFT_SHAPELESS(name,
				ECraftingType.About(Blocks.livingstone.asWItem(id).asWItemStack().changeCount(8)),
				Items.shale.asWItem(id),
				minerals, minerals, minerals, minerals,
				minerals, minerals, minerals, minerals
			)
		})
		Registrar.addRecipeCallback(s"smelting.$shalename", name => {
			ECraftingType.COOK_SMELT(name,
				ECraftingType.About(WItemStack.make(s"minecraft:${variant.name}_dye")),
				Items.shale.asWItem(id)
			)
		})
	}
}