package mod.iceandshadow3.world.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.common.LogicItemChameleon
import mod.iceandshadow3.basics.util.LogicPair
import mod.iceandshadow3.basics.{BLogicItem, BStateData}
import mod.iceandshadow3.compat.ResourceMap
import mod.iceandshadow3.compat.entity.WEntityPlayer
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.util.E3vl
import mod.iceandshadow3.world.DomainAlien

class LIFrozen extends LogicItemChameleon(DomainAlien, "item_frozen") {
	override def onUse(variant: Int, state: BStateData, stack: WItemStack, user: WEntityPlayer, mainhand: Boolean) = {
		if(!user.dimension.isHellish) {
			user.message("iced_over")
			E3vl.FALSE
		} else {
			user.give(new WItemStack(stack.exposeNbtTree().chroot(IaS3.MODID).chroot("itemstack"), null))
			stack.destroy()
			E3vl.TRUE
		}
	}

	override def addTooltip(variant: Int, what: WItemStack) = "iceandshadow3.tooltip.iced_over"

	//TODO: This could use its own model, one which loads the source model and adds ice textures to it.
}
object LIFrozen {
	private val MAGIC_ID_BREAKS = "~:B"
	private val MAGIC_ID_FREEZES = "~:F"
	private val MAGIC_ID_RESISTS = "~:R"
	private val unusualFreezeMap = new ResourceMap[String]
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
	itemFreezesAndChanges("minecraft:lava_bucket", "minecraft:obsidian")
	itemFreezesAndChanges("minecraft:water_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:cod_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:salmon_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:pufferfish_bucket", "minecraft:ice")
	itemFreezesAndChanges("minecraft:tropical_fish_bucket", "minecraft:ice")
	itemFreezesAndBreaks("minecraft:blaze_rod")
	itemFreezesAndBreaks("minecraft:blaze_powder")
	itemFreezesAndBreaks("minecraft:brewing_stand")
	itemFreezes("minecraft:flint_and_steel")
	itemFreezes("minecraft:clay")
	itemFreezes("minecraft:clay_ball")
	itemFreezes("minecraft:campfire")

	private val pair = new LogicPair[BLogicItem](DomainAlien.frozen, 0)
	def freeze(input: WItemStack): WItemStack = {
		val name = input.registryName
		if(name == null) return input //Also checks if the stack is empty.
		val resultNameOption = unusualFreezeMap.get(name)
		if(resultNameOption.isEmpty) {
			if(input.getDomain.resistsFreezing) input
			else if(input.getBurnTicks > 0 || input.isFoodOrDrink || input.isPotion) {
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