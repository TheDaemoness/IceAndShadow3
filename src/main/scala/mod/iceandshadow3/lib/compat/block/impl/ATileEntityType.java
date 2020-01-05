package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.LogicTileEntity;
import mod.iceandshadow3.lib.compat.nbt.NbtVarMap;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;

import java.util.List;
import java.util.Set;

public class ATileEntityType extends TileEntityType<ATileEntity> {
	static List<ATileEntityType> instances;

	public ATileEntityType(LogicTileEntity logic, int index, Set<Block> validBlocksIn) {
		super(() -> new ATileEntity(logic, new NbtVarMap(logic.variables()), index), validBlocksIn, null);
		this.setRegistryName(logic.id().asVanilla());
	}
}
