package mod.iceandshadow3.compat.item.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.compat.block.impl.ABlock;
import net.minecraft.item.BlockItem;

public class AItemBlock extends BlockItem {
	public AItemBlock(BLogicBlock logic, int variant, ABlock ab) {
		super(ab, logic.toItemProperties(variant));
		this.setRegistryName(IaS3.MODID, logic.getName(variant));
	}
}
