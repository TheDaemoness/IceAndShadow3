package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.LogicTileEntity;
import mod.iceandshadow3.lib.compat.nbt.NbtVarMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

final public class ATileEntity extends TileEntity {
	private final LogicTileEntity logic;
	final NbtVarMap vars;
	ATileEntity(LogicTileEntity logic, NbtVarMap vars, int typeIndex) {
		super(ATileEntityType.instances.get(typeIndex));
		this.logic = logic;
		this.vars = vars;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		final CompoundNBT nbt = new CompoundNBT();
		if(vars.writeChanges(nbt)) {
			return new SUpdateTileEntityPacket(this.pos, 0, nbt);
		} else return null;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		vars.loadFrom(pkt.getNbtCompound());
		this.markDirty();
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
		vars.writeNondefaults(nbt);
		return nbt;
	}
}
