package mod.iceandshadow3.lib.compat.forge.cap;

import mod.iceandshadow3.lib.compat.item.ItemUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

abstract class BAForgeItemHandler implements IItemHandler {
	protected final IInventory underlying;

	protected BAForgeItemHandler(IInventory inv) {
		this.underlying = inv;
	}

	public abstract int remap(int slot);

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return underlying.getStackInSlot(remap(slot));
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		final int slotRemapped = remap(slot);
		final ItemStack existing = underlying.getStackInSlot(slotRemapped);
		if(existing.isEmpty()) {
			if(!simulate) underlying.setInventorySlotContents(slotRemapped, stack);
			return ItemStack.EMPTY;
		} else {
			final ItemStack retval = stack.copy();
			if(simulate) {
				final ItemStack newContents = existing.copy();
				ItemUtils.stackInto(newContents, retval);
			} else {
				ItemUtils.stackInto(existing, retval);
				underlying.setInventorySlotContents(slotRemapped, existing);
			}
			return retval;
		}
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(!simulate) {
			return underlying.decrStackSize(remap(slot), amount);
		} else {
			final ItemStack simresult = underlying.getStackInSlot(remap(slot)).copy();
			simresult.setCount(Math.min(amount, simresult.getCount()));
			return simresult;
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		final int baselimit = underlying.getInventoryStackLimit();
		final ItemStack simresult = underlying.getStackInSlot(remap(slot));
		return simresult.isEmpty() ? baselimit : Math.min(baselimit, simresult.getMaxStackSize());
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return underlying.isItemValidForSlot(remap(slot), stack);
	}
}
