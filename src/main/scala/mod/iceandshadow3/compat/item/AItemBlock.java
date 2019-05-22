package mod.iceandshadow3.compat.item;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class AItemBlock extends ItemBlock {
	public AItemBlock(BLogicBlock logic, int variant) {
		super((Block) logic.getSecrets().get(variant), logic.toItemProperties(variant));
		this.setRegistryName(IaS3.MODID, logic.getName(variant));
	}
}
