package mod.iceandshadow3.lib.compat.inventory

import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import net.minecraft.inventory.IInventory

class WInventoryOwned[+Owner <: WEntity](inv: IInventory, val owner: Owner) extends WInventory(inv) {
	override def apply(idx: Int) = new WItemStackOwned(inv.getStackInSlot(idx), owner)
	override def iterator: Iterator[WItemStackOwned[Owner]] = new Iterator[WItemStackOwned[Owner]] {
		var index = 0
		override def hasNext = index < inv.getSizeInventory
		override def next() = {
			index += 1
			new WItemStackOwned(inv.getStackInSlot(index-1), owner)
		}
	}
}
