package mod.iceandshadow3.compat;

import java.util.List;

import mod.iceandshadow3.basics.BlockLogic;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;

public class ABlockMulti extends ABlock {
	protected final int variantbias;
	
	ABlockMulti(BlockLogic blocklogic, int variantstart) {
		super(blocklogic, variantstart);
		variantbias = variantstart;
	}
	@Override
	public int getVariantId(IBlockState bs) {
		return variantbias /*+ metadata or w/e*/;
	}
	@Override
	public int maxVariants() {
		return 16;
	}
	
	//Stubby by design. See ABlock for where most of the logic is.
}
