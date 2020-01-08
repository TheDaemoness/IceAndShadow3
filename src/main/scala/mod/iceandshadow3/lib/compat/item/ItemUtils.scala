package mod.iceandshadow3.lib.compat.item

import net.minecraft.item.ItemStack

private[compat] object ItemUtils {
	def stackInto(into: ItemStack, from: ItemStack): Int = {
		val canstack = if (into.isStackable) {
			into.equals(from, false)
		} else false
		if (canstack) { //May be able to stack. Do it.
			val adjustment = Math.min(into.getMaxStackSize - into.getCount, into.getCount)
			into.grow(adjustment)
			from.shrink(adjustment)
			adjustment
		} else 0
	}
}
