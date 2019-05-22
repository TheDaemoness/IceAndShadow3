package mod.iceandshadow3.compat.block;

import java.util.List;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.util.HarvestMethod;
import mod.iceandshadow3.basics.util.LogicPair;
import mod.iceandshadow3.compat.ILogicBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;

public class ABlock extends Block implements ILogicBlockProvider, IShearable {
	
	private final BLogicBlock logic;
	private final int variant;
	
	public ABlock(BLogicBlock blocklogic, int variant) {
		super(((BCompatLogicBlock)blocklogic).toBlockProperties(variant));
		logic = blocklogic;
		this.setRegistryName(IaS3.MODID, logic.getName(variant));
		this.variant = variant;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
		logic.isToolClassEffective(HarvestMethod.SHEAR);
		return null; //TODO: NO!
	}

	@Nonnull
	@Override
	public LogicPair<BLogicBlock> getLogicPair() {
		return LogicPair.apply(logic, variant);
	}
}

