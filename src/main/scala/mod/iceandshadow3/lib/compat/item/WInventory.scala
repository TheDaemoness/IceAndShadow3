package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.item.ItemSeq
import mod.iceandshadow3.lib.util.E3vl
import net.minecraft.inventory.IInventory

class WInventory(inv: IInventory) extends ItemSeq {
	def add(what: WItemStack): Boolean = {
		if (what.isEmpty) return true
		val whatexposed = what.asItemStack()
		for (i <- 0 until inv.getSizeInventory) {
			val current = inv.getStackInSlot(i)
			if (current == null || current.isEmpty) { //Empty slot.
				if (inv.isItemValidForSlot(i, whatexposed)) {
					inv.setInventorySlotContents(i, what.move())
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

	/** Adds this item if a stack of the same type does not already exist.
		* @return True if the item was added, neutral if it existed, and false if it couldn't be added.
		*/
	def donate(item: WItemStack): E3vl = {
		var slot = -1
		for(i <- 0 until inv.getSizeInventory) {
			val stack = inv.getStackInSlot(i)
			if(slot == -1 && (stack == null || stack.isEmpty) && inv.isItemValidForSlot(i, item.asItemStack())) slot = i
			if(item.matches(stack)) return E3vl.NEUTRAL
		}
		if(slot == -1) E3vl.FALSE
		else {inv.setInventorySlotContents(slot, item.move()); E3vl.TRUE}
	}

	override def update(idx: Int, elem: WItemStack): Unit = {
		val stack = elem.asItemStack()
		if(inv.isItemValidForSlot(idx, stack)) inv.setInventorySlotContents(idx, stack)
		else inv.removeStackFromSlot(idx)
	}
	override def length = inv.getSizeInventory
	override def apply(idx: Int) = new WItemStack(inv.getStackInSlot(idx))
	override def iterator: Iterator[WItemStack] = new Iterator[WItemStack] {
		var index = 0
		override def hasNext = index < inv.getSizeInventory

		override def next() = {
			index += 1
			new WItemStack(inv.getStackInSlot(index-1))
		}
	}
}
