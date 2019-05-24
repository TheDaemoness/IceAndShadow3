package mod.iceandshadow3.compat.item

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
		* @return False if the inventory doesn't contain an item of the matching type after this.
		*/
	def donate(item: CRefItem): Boolean = {
		var slot = -1
		for(i <- 0 until inv.getSizeInventory) {
			val stack = inv.getStackInSlot(i)
			if(slot == -1 && (stack == null || stack.isEmpty) && inv.isItemValidForSlot(i, item.exposeItems())) slot = i
			if(item.matches(stack)) return true
		}
		if(slot == -1) false
		else {inv.setInventorySlotContents(slot, item.move()); true}
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
