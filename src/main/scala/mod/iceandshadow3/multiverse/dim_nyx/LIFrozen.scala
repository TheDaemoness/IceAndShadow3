package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.item.{WItemStack, WUsageItem}
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.common.LogicItemChameleon
import mod.iceandshadow3.lib.compat.id.WIdTagItem
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainAlien

object LIFrozen {
	val tagAntifreeze = WIdTagItem("iceandshadow3:antifreeze")
	val tagFreezes = WIdTagItem("iceandshadow3:freezes")

	ItemFreezability.enable()

	def freeze(input: WItemStack, world: WWorld): WItemStack = {
		val name = input.registryName
		if(name == null) return input //Also checks if the stack is empty.
		val freezability = world.apply(ItemFreezability, input.asWItem())
		if(freezability.fold(false)(_.freezes)) {
			if(freezability.orNull.unusual) {
				val newname = UnusualFreezeMap(name).orNull
				if(newname == null) WItemStack.empty
				else WItemStack.make(newname).setCount(input.count)
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
	override def onUseGeneral(context: WUsageItem) = {
		val hellish = E3vl.fromBool(context.stack.dimension.isHellish)
		hellish.forBoolean(b => {
			if(b) {
				context.stack.owner.give(context.stack(LogicItemChameleon.varItemWrapped))
				context.stack.destroy()
			} else context.stack.owner.message("iced_over")
		})
		hellish
	}

	override val handlerTooltip = _ => "iceandshadow3.tooltip.iced_over"

	//TODO: This could use its own model, one which loads the source model and adds ice textures to it.
}