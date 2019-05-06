package mod.iceandshadow3.compat.item;

import mod.iceandshadow3.IceAndShadow3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.compat.block.BCompatLogicBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class AItemBlock extends ItemBlock {
	public AItemBlock(BLogicBlock logic, int variant) {
		super((Block) logic.getSecrets().get(variant), ((BCompatLogicBlock)logic).toItemProperties(variant));
		this.setRegistryName(IceAndShadow3.MODID, logic.getName(variant));
	}
}
