package mod.iceandshadow3.compat.block;

import java.util.List;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.util.HarvestMethod;
import mod.iceandshadow3.basics.util.ILogicProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.IShearable;

public class ABlock extends Block implements ILogicProvider<BLogicBlock>, IShearable {
	
	private final BLogicBlock bl;
	private final int variant;
	
	public ABlock(BLogicBlock blocklogic, int variant) {
		super(((BCompatLogicBlock)blocklogic).toBlockProperties(variant));
		bl = blocklogic;
		this.setRegistryName(IaS3.MODID, bl.getName(variant));
		this.variant = variant;
	}
	public int getVariantId(IBlockState bs) {
		return 0;
	}
	
	@Override
	public BLogicBlock getLogic() {
		return bl;
	}

	@Override
	public int getVariant() {
		return variant;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
		bl.isToolClassEffective(HarvestMethod.SHEAR);
		return null;
	}
}

