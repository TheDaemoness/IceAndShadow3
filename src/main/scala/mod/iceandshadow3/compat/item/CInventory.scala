package mod.iceandshadow3.compat.item

import net.minecraft.inventory.IInventory

//TODO: Incomplete.
class CInventory(inv: IInventory) {
	def add(what: CRefItem): Boolean = {
		if (what.isEmpty) return true
		val whatexposed = what.exposeItems()
		for (i <- 0 until inv.getSizeInventory) {
			val current = inv.getStackInSlot(i)
			if (current == null || current.isEmpty) { //Empty slot.
				if (inv.isItemValidForSlot(i, whatexposed)) {
					inv.setInventorySlotContents(i, whatexposed.copy())
					what.destroy()
					return true
				}
			} else { //Not an empty slot, try to stack.
				val canstack = if (current.isStackable) {
					//TODO: More involved checks.
					current.isItemEqual(whatexposed)
				} else false
				if (canstack) { //May be able to stack. Do it.
					val adjustment = Math.min(current.getMaxStackSize - current.getCount, whatexposed.getCount)
					current.grow(adjustment)
					whatexposed.shrink(adjustment)
					if (adjustment != 0) {
						inv.markDirty()
						if (what.isEmpty) return true
					}
				}
			}
		}
		false
	}
}
