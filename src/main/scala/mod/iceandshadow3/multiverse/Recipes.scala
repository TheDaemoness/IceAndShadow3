package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.compat.WIdItem
import mod.iceandshadow3.lib.compat.item.{WInventoryCrafting, WItemStack}
import mod.iceandshadow3.lib.compat.recipe.{ECraftingType, ERecipeSize}
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.item.LogicCrafting
import mod.iceandshadow3.multiverse.gaia.{ELivingstoneTypes, LIMinerals}
import mod.iceandshadow3.multiverse.polis.LIMoonstoneDust

object Recipes { private[iceandshadow3] def apply(): Boolean = {
	import mod.iceandshadow3.lib.compat.recipe.CraftResult.apply
	import ERecipeSize._

	val minerals = DomainGaia.Items.minerals
	for(variant <- ELivingstoneTypes.values()) {
		val id = variant.ordinal()
		ECraftingType.COOK_SMELT(WIdItem(s"minecraft:${variant.name}_dye"), DomainGaia.Items.shales(id)).register()
		ECraftingType.COOK_SMELT(DomainPolis.Blocks.stones(id), DomainGaia.Blocks.livingstones(id)).register()
		ECraftingType.CRAFT_SHAPELESS(DomainGaia.Blocks.livingstones(id),
			DomainGaia.Items.shales(id),
			minerals, minerals, minerals, minerals, minerals, minerals, minerals, minerals //x8
		).transformResult(_.setCount(8)).suffix("mineralize").register()
	}

	ECraftingType.CRAFT_SPECIAL.register(new LogicCrafting("grow_with_minerals") {
		override def fitsIn(width: Int, height: Int) = width*height >= 2
		override def matches(what: WInventoryCrafting, world: WWorld): Boolean = {
			var foundMinerals = false
			var foundGrowable = false
			for(item <- what) {
				if(!foundMinerals && item.matches(minerals)) foundMinerals = true
				else if(!foundGrowable && item.toBlockState.fold(false)(LIMinerals.canGrow)) foundGrowable = true
				else if(!item.isEmpty) return false
			}
			foundGrowable && foundMinerals
		}
		override def apply(what: WInventoryCrafting): WItemStack = {
			for(item <- what) {
				if(!item.isEmpty && !item.matches(minerals)) return item.copy.setCount(2)
			}
			WItemStack.empty
		}
	})

	ECraftingType.CRAFT_SHAPELESS(DomainPolis.Items.moonstone_dust,
		DomainGaia.Items.moonstone
	).transformResult(
		stack => stack.setDamage(stack.getDamageMax - LIMoonstoneDust.DUST_PER_ITEM)
	).suffix("crush").register()

	ECraftingType.CRAFT_SHAPELESS(DomainPolis.Items.petrified_brick,
		DomainGaia.Blocks.petrified_log
	).transformResult(_.setCount(4)).suffix("from_wood").register()
	ECraftingType.registerAB(DomainNyx.Items.nifelhium_small, DomainNyx.Items.nifelhium)
	ECraftingType.registerAB(DomainPolis.Items.petrified_brick, DomainPolis.Blocks.petrified_bricks, TWO_X_TWO)
	ECraftingType.registerAB(DomainGaia.Items.devora_small, DomainGaia.Items.devora)
	ECraftingType.CRAFT_SHAPELESS(DomainGaia.Items.cortra_dust, DomainGaia.Items.cortra).suffix("crush").register()
	ECraftingType.COOK_SMELT(DomainGaia.Items.cortra, DomainGaia.Items.cortra_dust).register()
	ECraftingType.CRAFT_SHAPELESS(DomainNyx.Items.bone,
		WIdItem("minecraft:bone"), minerals
	).suffix("mineralize").register()
}}
