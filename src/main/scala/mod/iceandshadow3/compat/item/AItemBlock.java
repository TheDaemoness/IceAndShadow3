package mod.iceandshadow3.compat.item;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.compat.block.ABlock;
import net.minecraft.item.ItemBlock;

public class AItemBlock extends ItemBlock {
	public AItemBlock(BLogicBlock logic, int variant, ABlock ab) {
		super(ab, logic.toItemProperties(variant));
		this.setRegistryName(IaS3.MODID, logic.getName(variant));
	}
}
