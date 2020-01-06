package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.LogicTileEntity;
import mod.iceandshadow3.lib.compat.nbt.NbtVarMap;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

import java.util.Set;

public class ATileEntityType extends TileEntityType<ATileEntity> {
	public ATileEntityType(LogicTileEntity logic, Set<Block> validBlocksIn) {
		super(() -> new ATileEntity(logic, new NbtVarMap(logic.variables())), validBlocksIn, null);
		this.setRegistryName(logic.id().asVanilla());
	}
}
