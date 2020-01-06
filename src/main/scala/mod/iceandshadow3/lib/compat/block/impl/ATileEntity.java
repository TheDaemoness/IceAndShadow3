package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.LogicTileEntity;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.item.WItemStack;
import mod.iceandshadow3.lib.compat.nbt.NbtTagUtils;
import mod.iceandshadow3.lib.compat.nbt.NbtVarMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

final public class ATileEntity extends TileEntity implements IInventory {
	public static String inventoryKey = "items";
	//TODO: Delta updates using Forge-based netcode.
	//TODO: The inventory stuff could use its own class.
	private final ItemStack[] inventory;
	private int filledSlots;

	private void triggerUpdate() {
		markDirty();
	}
	private ItemStack exchange(int index, @Nonnull ItemStack nu) {
		if(index < inventory.length) {
			final ItemStack old = inventory[index];
			inventory[index] = nu;
			if(old.isEmpty() && !nu.isEmpty()) ++filledSlots;
			else if(!old.isEmpty() && nu.isEmpty()) --filledSlots;
			triggerUpdate();
			return old;
		}
		return ItemStack.EMPTY;
	}

	private final LogicTileEntity logic;
	final NbtVarMap vars;

	private void doReadNbt(CompoundNBT nbt) {
		vars.loadFrom(nbt);
		doClear();
		if(nbt.contains(inventoryKey, NbtTagUtils.ID_LIST())) {
			final ListNBT inv = nbt.getList(inventoryKey, NbtTagUtils.ID_COMPOUND());
			for(int i = 0; i < inv.size() && i < inventory.length; ++i) {
				final ItemStack is = ItemStack.read(inv.getCompound(i));
				if(!is.isEmpty()) {
					inventory[i] = is;
					++filledSlots;
				}
			}
		}
	}
	private void doWriteNbt(CompoundNBT nbt) {
		vars.writeTo(nbt);
		final ListNBT inv = new ListNBT();
		for(int i = 0, n = 0; i < inventory.length && n < filledSlots; ++i) {
			final ItemStack is = inventory[i];
			inv.add(is.serializeNBT());
			if(!is.isEmpty()) ++n;
		}
		nbt.put(inventoryKey, inv);
	}

	ATileEntity(LogicTileEntity logic, NbtVarMap vars) {
		super((ATileEntityType)BinderTileEntity.apply(logic));
		this.logic = logic;
		this.vars = vars;
		this.inventory = new ItemStack[logic.itemCapacity()];
		doClear();
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		doWriteNbt(compound);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		doReadNbt(compound);
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag() {
		//Misnamed: function is called on chunk load.
		final CompoundNBT nbt = super.getUpdateTag();
		doWriteNbt(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		//Misnamed: function is called on chunk load.
		doReadNbt(tag);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		final CompoundNBT nbt = new CompoundNBT();
		doWriteNbt(nbt);
		return new SUpdateTileEntityPacket(this.pos, -1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		doReadNbt(pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = super.serializeNBT();
		doWriteNbt(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		doReadNbt(nbt);
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
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public boolean isEmpty() {
		return filledSlots <= 0;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index) {
		if(index < inventory.length) return inventory[index];
		else return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count) {
		if(index < inventory.length) {
			final ItemStack is = inventory[index];
			if (count >= is.getCount() && !is.isEmpty()) {
				inventory[index] = ItemStack.EMPTY;
				--filledSlots;
				triggerUpdate();
			} else {
				final ItemStack r = is.copy();
				r.setCount(is.getCount() - count);
				inventory[index] = r;
				is.setCount(count);
				triggerUpdate();
			}
			return is;
		}
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return exchange(index, ItemStack.EMPTY);
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
		exchange(index, stack);
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
		return logic.isUsableBy(CNVEntity.wrap(player));
	}

	private void doClear() {
		filledSlots = 0;
		Arrays.fill(inventory, ItemStack.EMPTY);
	}

	@Override
	public void clear() {
		triggerUpdate();
		filledSlots = 0;
		Arrays.fill(inventory, ItemStack.EMPTY);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return logic.canStore(index, new WItemStack(stack));
	}
}
