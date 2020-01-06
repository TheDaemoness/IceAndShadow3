package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.LogicTileEntity;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.item.WItemStack;
import mod.iceandshadow3.lib.compat.nbt.NbtVarMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

final public class ATileEntity extends TileEntity implements IInventory {
	private final ItemStack[] inventory;
	private int filledSlots = 0;
	private ItemStack exchange(int index, ItemStack stack) {
		if(index < inventory.length) {
			final ItemStack old = inventory[index];
			final ItemStack nu = stack == ItemStack.EMPTY ? null : stack;
			inventory[index] = nu;
			if(old == null && nu != null) ++filledSlots;
			else if(old != null && nu == null) --filledSlots;
			return old;
		}
		return null;
	}
	@Nonnull
	private ItemStack filterNull(@Nullable ItemStack what) {
		return what == null ? ItemStack.EMPTY : what;
	}

	private final LogicTileEntity logic;
	final NbtVarMap vars;

	ATileEntity(LogicTileEntity logic, NbtVarMap vars, int typeIndex) {
		super(ATileEntityType.instances.get(typeIndex));
		this.logic = logic;
		this.vars = vars;
		this.inventory = new ItemStack[logic.itemCapacity()];
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		final CompoundNBT nbt = new CompoundNBT();
		vars.writeTo(nbt);
		return new SUpdateTileEntityPacket(this.pos, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		vars.loadFrom(pkt.getNbtCompound());
	}

	@Override
	public void onChunkUnloaded() {
		logic.onUnload(vars);
	}

	@Override
	public void onLoad() {
		logic.onLoad(vars);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		vars.loadFrom(nbt);
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = new CompoundNBT();
		vars.writeTo(nbt);
		return nbt;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public boolean isEmpty() {
		return filledSlots > 0;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index) {
		if(index < inventory.length) return filterNull(inventory[index]);
		else return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count) {
		if(index < inventory.length) {
			final ItemStack is = inventory[index];
			if(is != null) {
				if (count >= is.getCount()) {
					inventory[index] = null;
					--filledSlots;
				} else {
					is.setCount(is.getCount() - count);
					return is;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return filterNull(exchange(index, null));
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		exchange(index, stack);
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
		return logic.isUsableBy(CNVEntity.wrap(player));
	}

	@Override
	public void clear() {
		filledSlots = 0;
		Arrays.fill(inventory, null);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return logic.canStore(index, new WItemStack(stack));
	}
}
