package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.item.{WItemStack, WUsageItem}
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.subtype.LogicItemChameleon
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainAlien

object LIFrozen {

	ItemFreezability.enable()

	def freeze(input: WItemStack, world: WWorld): WItemStack = {
		val name = input.registryName
		if(name == null) return input //Also checks if the stack is empty.
		val freezability = world.apply(ItemFreezability, input.asWItem())
		if(freezability.fold(false)(_.freezes)) {
			if(freezability.orNull.unusual) {
				val newname = UnusualFreezeMap(name).orNull
				if(newname == null) new WItemStack(null, null)
				else WItemStack.make(newname).changeCount(input.count)
			} else LogicItemChameleon.createFrom(input, DomainAlien.frozen)
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
				context.user.give(context.stack(LogicItemChameleon.varItemWrapped))
				context.stack.destroy()
			} else context.user.message("iced_over")
		})
		hellish
	}

	override def addTooltip(variant: Int, what: WItemStack) = "iceandshadow3.tooltip.iced_over"

	//TODO: This could use its own model, one which loads the source model and adds ice textures to it.
}