package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.BlockLogic;
import net.minecraft.block.state.IBlockState;

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
