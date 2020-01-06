package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.LogicTileEntity;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.inventory.InventoryImpl;
import mod.iceandshadow3.lib.compat.item.WItemStack;
import mod.iceandshadow3.lib.compat.nbt.NbtVarMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

final public class ATileEntity extends TileEntity implements IInventory {
	private final InventoryImpl inventory;
	private final LogicTileEntity logic;
	final NbtVarMap vars;

	public static String inventoryKey = "items";

	ATileEntity(LogicTileEntity logic, NbtVarMap vars) {
		super((ATileEntityType)BinderTileEntity.apply(logic));
		this.logic = logic;
		this.vars = vars;
		this.inventory = new InventoryImpl(logic.itemCapacity(), inventoryKey);
	}

	private void triggerUpdate() {
		//TODO: Delta updates using Forge-based netcode.
		final BlockState bs = this.getBlockState();
		if(world != null) world.notifyBlockUpdate( pos, bs, bs, 2);
		markDirty();
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		vars.writeNbt(compound);
		inventory.writeNbt(compound);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		inventory.readNbt(compound);
		vars.readNbt(compound);
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag() {
		//Misnamed: function is called on chunk load.
		final CompoundNBT nbt = super.getUpdateTag();
		vars.writeNbt(nbt);
		if(logic.syncInventoryOnLoad()) inventory.writeNbt(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		//Misnamed: function is called on chunk load.
		vars.readNbt(tag);
		if(logic.syncInventoryOnLoad()) inventory.readNbt(tag);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		final CompoundNBT nbt = new CompoundNBT();
		vars.writeNbt(nbt);
		inventory.writeNbt(nbt);
		return new SUpdateTileEntityPacket(this.pos, -1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		final CompoundNBT nbt = pkt.getNbtCompound();
		inventory.readNbt(nbt);
		vars.readNbt(nbt);
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = super.serializeNBT();
		vars.writeNbt(nbt);
		inventory.writeNbt(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		inventory.readNbt(nbt);
		vars.readNbt(nbt);
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
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.apply(index);
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count) {
		final ItemStack retval = inventory.splitFrom(index, count);
		if(!retval.isEmpty()) triggerUpdate();
		return retval;
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		final ItemStack retval = inventory.take(index);
		if(!retval.isEmpty()) triggerUpdate();
		return retval;
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
		if(inventory.update(index, stack)) triggerUpdate();
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
		return logic.isUsableBy(CNVEntity.wrap(player));
	}

	@Override
	public void clear() {
		inventory.clear();
		triggerUpdate();
	}

	public int countFilled() {
		return inventory.countFilled();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return logic.canStore(index, new WItemStack(stack));
	}
}
