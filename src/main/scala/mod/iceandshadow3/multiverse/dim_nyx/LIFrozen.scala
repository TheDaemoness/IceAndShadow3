package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.subtype.LogicItemChameleon
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.item.{ItemQueries, WItemStack, WUseContext}
import mod.iceandshadow3.lib.compat.misc.ResourceMap
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainAlien

object LIFrozen {
	private val MAGIC_ID_BREAKS = "~:B"
	private val MAGIC_ID_FREEZES = "~:F"
	private val MAGIC_ID_RESISTS = "~:R"
	private val unusualFreezeMap = new ResourceMap[String]

	itemDoesNotFreeze("minecraft:blaze_rod")
	itemDoesNotFreeze("minecraft:blaze_powder")
	itemDoesNotFreeze("minecraft:brewing_stand")
	itemFreezesAndChanges("minecraft:lava_bucket", "minecraft:obsidian")
	itemFreezesAndChanges("minecraft:water_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:cod_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:salmon_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:pufferfish_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:tropical_fish_bucket", "minecraft:ice")
	itemFreezesAndBreaks("minecraft:fire_charge")
	itemFreezesAndBreaks("minecraft:magma_cream")
	itemFreezesAndBreaks("minecraft:magma_block")
	itemFreezes("minecraft:slime_ball")
	itemFreezes("minecraft:slime_block")
	itemFreezes("minecraft:flint_and_steel")
	itemFreezes("minecraft:clay")
	itemFreezes("minecraft:clay_ball")
	itemFreezes("minecraft:campfire")
	itemFreezes("minecraft:torch")
	itemFreezes("minecraft:firework_rocket")
	itemFreezes("minecraft:firework_star")
	itemFreezes("minecraft:gunpowder")
	//TODO: Also freeze items if they have a banned item in a crafting recipe (and disable this behavior for sticks).

	def itemDoesNotFreeze(id: String): Unit = {
		unusualFreezeMap += Tuple2(id, MAGIC_ID_RESISTS)
	}
	def itemFreezes(id: String): Unit = {
		unusualFreezeMap += Tuple2(id, MAGIC_ID_FREEZES)
	}
	def itemFreezesAndBreaks(id: String): Unit = {
		unusualFreezeMap += Tuple2(id, MAGIC_ID_BREAKS)
	}
	def itemFreezesAndChanges(id: String, to: String): Unit = {
		//TODO: Check that to is valid.
		unusualFreezeMap += Tuple2(id, to)
	}
	def freeze(input: WItemStack): WItemStack = {
		val name = input.registryName
		if(name == null) return input //Also checks if the stack is empty.
		val resultNameOption = unusualFreezeMap.get(name)
		if(resultNameOption.isEmpty) {
			if(input.getDomain.resistsFreezing) input
			else if(input.getBurnTicks > 0 || input.isAny(ItemQueries.food, ItemQueries.drink, ItemQueries.compostable)) {
				LogicItemChameleon.createFrom(input, DomainAlien.frozen)
			} else input
		} else {
			val newname = resultNameOption.get
			if(newname.eq(MAGIC_ID_RESISTS)) input
			else if(newname.eq(MAGIC_ID_BREAKS)) new WItemStack(null, null)
			else if(newname.eq(MAGIC_ID_FREEZES)) LogicItemChameleon.createFrom(input, DomainAlien.frozen)
			else WItemStack.make(newname) //Make recursive? Then we'd need to check for circular references.
		}
	}

	def freeze(input: WItemStack, player: Option[WEntityPlayer]): Option[WItemStack] = {
		val result = freeze(input)
		if(result != input) {
			player.foreach(p => if(p.isServerSide) p.message("item_freezes", false, input))
			//TODO: Play sound.
			Some(result)
		} else None
	}
}

class LIFrozen extends LogicItemChameleon(DomainAlien, "item_frozen") {
	override def onUseGeneral(variant: Int, context: WUseContext) = {
		val hellish = E3vl.fromBool(context.user.dimension.isHellish)
		hellish.forBoolean({
			if(_) {
				context.user.give(new WItemStack(context.stack.exposeNbtTree().chroot(IaS3.MODID).chroot("itemstack"), null))
				context.stack.destroy()
			} else context.user.message("iced_over")
		})
		hellish
	}

	override def addTooltip(variant: Int, what: WItemStack) = "iceandshadow3.tooltip.iced_over"

	//TODO: This could use its own model, one which loads the source model and adds ice textures to it.
}