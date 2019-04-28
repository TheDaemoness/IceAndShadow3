package mod.iceandshadow3.compat;

import java.util.List;

import mod.iceandshadow3.basics.BlockLogic;
import mod.iceandshadow3.basics.IBlockLogicProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.IShearable;

public class ABlock extends Block implements IBlockLogicProvider, IShearable {
	
	protected final BlockLogic bl;
	
	//Critical that all BlockLogic adapters have a BlockLogic, int/Integer constructor.
	ABlock(BlockLogic blocklogic, int variantbias) {
		super(SLogicToProperties.convert(blocklogic));
		bl = blocklogic;
		//TODO: Creative tab.
	}
	public int getVariantId(IBlockState bs) {
		return 0;
	}
	@Override
	public int maxVariants() {
		return 1;
	}
	
	@Override
	public BlockLogic getBlockLogic() {
		return bl;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
		bl.isToolClassEffective(HarvestMethod.SHEAR);
		return null;
	}
}

