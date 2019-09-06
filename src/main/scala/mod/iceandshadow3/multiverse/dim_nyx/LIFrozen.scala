package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.item.{WItemStack, WUsageItem}
import mod.iceandshadow3.lib.compat.misc.ResourceMap
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.subtype.LogicItemChameleon
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainAlien

object LIFrozen {
	ItemFreezability.enable()
	private val unusualFreezeMap = new ResourceMap[String]

	val tagHot = "iceandshadow3:hot"
	val tagFreezes = "iceandshadow3:freezes"

	//TODO: Allow specifying these in recipes.
	itemFreezesDifferently("minecraft:lava_bucket", "minecraft:obsidian")
	itemFreezesDifferently("minecraft:water_bucket", "minecraft:ice")
	itemFreezesDifferently("minecraft:cod_bucket", "minecraft:ice")
	itemFreezesDifferently("minecraft:salmon_bucket", "minecraft:ice")
	itemFreezesDifferently("minecraft:pufferfish_bucket", "minecraft:ice")
	itemFreezesDifferently("minecraft:tropical_fish_bucket", "minecraft:ice")
	itemFreezesDifferently("minecraft:sticky_piston", "minecraft:piston")
	//TODO: Also freeze items if they have a banned item in a crafting recipe (and disable this behavior for sticks).

	def itemFreezesDifferently(id: String, to: String): Unit = {
		//TODO: Check that to is valid.
		unusualFreezeMap += Tuple2(id, to)
	}
	def freeze(input: WItemStack, world: WWorld): WItemStack = {
		val name = input.registryName
		if(name == null) return input //Also checks if the stack is empty.
		val freezability = world.apply(ItemFreezability, input.asWItem())
		if(freezability.fold(false)(_.freezes)) {
			val newname = unusualFreezeMap.get(name).orNull
			if(newname == null) {
				//If we're here, the freezability option is definitely Some.
				if(freezability.orNull.antifreeze) new WItemStack(null, null)
				else LogicItemChameleon.createFrom(input, DomainAlien.frozen)
			} else WItemStack.make(newname)
		} else input
	}

	def freeze(input: WItemStack, world: WWorld, player: Option[WEntityPlayer]): Option[WItemStack] = {
		val result = freeze(input, world)
		if(result != input) {
			player.foreach(p => if(p.isServerSide) p.message("item_freezes", false, input))
			//TODO: Play sound.
			Some(result)
		} else None
	}
}

class LIFrozen extends LogicItemChameleon(DomainAlien, "item_frozen") {
	override def onUseGeneral(variant: Int, context: WUsageItem) = {
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