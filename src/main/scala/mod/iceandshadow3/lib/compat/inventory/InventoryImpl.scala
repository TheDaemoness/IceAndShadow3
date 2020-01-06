package mod.iceandshadow3.lib.compat.inventory

import mod.iceandshadow3.lib.compat.nbt.NbtTagUtils
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{CompoundNBT, ListNBT}

private[compat] class InventoryImpl(val size: Int, val nbtKey: String = InventoryImpl.nbtKey) {
	private var filled = 0
	private val inventory = Array.fill(size)(ItemStack.EMPTY)

	def writeNbt(nbt: CompoundNBT): Unit = {
		nbt.put(nbtKey, {
			val retval = new ListNBT
			var i = 0
			var n = 0
			while(n < filled && i < inventory.length) {
				val is = inventory(i)
				retval.add(is.serializeNBT())
				if(!is.isEmpty) n += 1
				i += 1
			}
			retval
		})
	}

	def readNbt(nbt: CompoundNBT): Unit = {
		if (nbt.contains(nbtKey, NbtTagUtils.ID_LIST)) {
			val inv = nbt.getList(nbtKey, NbtTagUtils.ID_COMPOUND)
			var i = 0
			while (i < inv.size && i < inventory.length) {
				val is = ItemStack.read(inv.getCompound(i))
				if (!is.isEmpty) {
					inventory(i) = is
					filled += 1
				}
				i += 1
			}
		}
	}

	def countFilled = filled
	def isEmpty = filled <= 0
	def apply(index: Int) = inventory.applyOrElse(index, (_: Int) => ItemStack.EMPTY)

	def take(index: Int): ItemStack = {
		val retval = apply(index)
		if(!retval.isEmpty) {
			filled -= 1
			inventory(index) = ItemStack.EMPTY
		}
		retval
	}

	def update(index: Int, nu: ItemStack): Boolean = nu != {
		if (index < inventory.length) {
			val old = inventory(index)
			inventory(index) = nu
			if (old.isEmpty && !nu.isEmpty) filled += 1
			else if (!old.isEmpty && nu.isEmpty) filled -= 1
			old
		} else ItemStack.EMPTY
	}

	def splitFrom(index: Int, count: Int): ItemStack = {
		if (index < inventory.length) {
			val is = inventory(index)
			if (count >= is.getCount && !is.isEmpty) {
				inventory(index) = ItemStack.EMPTY
				filled -= 1
			}
			else {
				val r = is.copy
				r.setCount(is.getCount - count)
				inventory(index) = r
				is.setCount(count)
			}
			return is
		}
		ItemStack.EMPTY
	}

	def clear() = {
		filled = 0
		inventory.mapInPlace((_: ItemStack) => ItemStack.EMPTY)
	}
}
object InventoryImpl {
	val nbtKey = "items"
	val none = new InventoryImpl(0)
}