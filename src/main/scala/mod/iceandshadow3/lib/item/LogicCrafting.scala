package mod.iceandshadow3.lib.item

import mod.iceandshadow3.lib.compat.inventory.WInventoryCrafting
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.world.WWorld

abstract class LogicCrafting(val name: String) {
	def fitsIn(width: Int, height: Int): Boolean
	def matches(what: WInventoryCrafting, world: WWorld) = !apply(what).isEmpty
	def apply(what: WInventoryCrafting): WItemStack
	def leftovers(what: WInventoryCrafting): ItemSeq =
		what.copy.mapInPlace(_.getContainerStack.getOrElse(WItemStack.empty))
}
