package mod.iceandshadow3.compat.block;

import java.util.List;

import mod.iceandshadow3.IceAndShadow3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.HarvestMethod;
import mod.iceandshadow3.basics.ILogicProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.IShearable;

public class ABlock extends Block implements ILogicProvider<BLogicBlock>, IShearable {
	
	protected final BLogicBlock bl;
	
	public ABlock(BLogicBlock blocklogic, int variant) {
		super(((BCompatLogicBlock)blocklogic).toProperties());
		bl = blocklogic;
		this.setRegistryName(IceAndShadow3.MODID, bl.getName(variant));
		//TODO: Creative tab.
	}
	public int getVariantId(IBlockState bs) {
		return 0;
	}
	
	@Override
	public BLogicBlock getLogic() {
		return bl;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
		bl.isToolClassEffective(HarvestMethod.SHEAR);
		return null;
	}
}

