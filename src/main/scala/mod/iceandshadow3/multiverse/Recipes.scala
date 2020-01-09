package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.compat.WIdItem
import mod.iceandshadow3.lib.compat.inventory.WInventoryCrafting
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.recipe.{ECraftingType, ERecipeSize, IngredientFactory}
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.item.LogicCrafting
import mod.iceandshadow3.multiverse.gaia.{ELivingstoneType, LIMinerals}
import mod.iceandshadow3.multiverse.polis.{LIMoonstoneDust, PetrifiedBricksUtils}

object Recipes { private[iceandshadow3] def apply(): Unit = {
	import mod.iceandshadow3.lib.compat.recipe.CraftResult.apply
	import ERecipeSize._

	val minerals = DomainGaia.Items.minerals
	for(variant <- ELivingstoneType.values()) {
		val id = variant.ordinal()
		ECraftingType.COOK_SMELT(WIdItem(s"minecraft:${variant.name}_dye"), DomainGaia.Items.shales(id)).register()
		ECraftingType.COOK_SMELT(DomainPolis.Blocks.stones(id).block, DomainGaia.Blocks.livingstones(id)).register()
		ECraftingType.CRAFT_SHAPELESS(DomainGaia.Blocks.livingstones(id),
			DomainGaia.Items.shales(id),
			minerals, minerals, minerals, minerals, minerals, minerals, minerals, minerals //x8
		).alterResult(_.setCount(8)).suffix("mineralize").register()
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
	).alterResult(
		stack => stack.setDamage(stack.getDamageMax - LIMoonstoneDust.DUST_PER_ITEM)
	).suffix("crush").register()

	ECraftingType.CRAFT_SHAPELESS(DomainPolis.Items.petrified_brick,
		DomainGaia.Blocks.petrified_log
	).alterResult(_.setCount(4)).suffix("from_wood").register()
	PetrifiedBricksUtils.recipes()

	ECraftingType.registerAB(DomainNyx.Items.nifelhium_small, DomainNyx.Items.nifelhium)
	ECraftingType.registerAB(DomainGaia.Items.moonstone, DomainGaia.Blocks.moonstone_block)
	ECraftingType.registerAB(DomainGaia.Items.devora_small, DomainGaia.Items.devora)
	ECraftingType.CRAFT_SHAPELESS(DomainGaia.Items.cortra_dust, DomainGaia.Items.cortra).suffix("crush").register()
	ECraftingType.COOK_SMELT(DomainGaia.Items.cortra, DomainGaia.Items.cortra_dust).register()
	ECraftingType.CRAFT_SHAPELESS(DomainNyx.Items.bone,
		WIdItem("minecraft:bone"), minerals
	).suffix("mineralize").unlockDeduce.register()

	ECraftingType.COOK_BLAST(WIdItem("minecraft:ender_pearl"),
		DomainNyx.Items.wayfinder
	).ticks(400).xp(13f).name("destroy_wayfinder").register()
}}
