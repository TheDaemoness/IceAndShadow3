package mod.iceandshadow3.compat.item

import mod.iceandshadow3.util.L3
import net.minecraft.entity.EntityLivingBase
import net.minecraft.inventory.IInventory

//TODO: Incomplete.
class CInventory(inv: IInventory, owner: EntityLivingBase = null) extends Iterable[CRefItem] {
	def add(what: CRefItem): Boolean = {
		if (what.isEmpty) return true
		val whatexposed = what.exposeItems()
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
	def donate(item: CRefItem): L3 = {
		var slot = -1
		for(i <- 0 until inv.getSizeInventory) {
			val stack = inv.getStackInSlot(i)
			if(slot == -1 && (stack == null || stack.isEmpty) && inv.isItemValidForSlot(i, item.exposeItems())) slot = i
			if(item.matches(stack)) return L3.NEUTRAL
		}
		if(slot == -1) L3.FALSE
		else {inv.setInventorySlotContents(slot, item.move()); L3.TRUE}
	}

	override def iterator = new Iterator[CRefItem] {
		var index = 0
		override def hasNext = index < inv.getSizeInventory

		override def next() = {
			index += 1
			new CRefItem(inv.getStackInSlot(index-1), owner)
		}
	}
}