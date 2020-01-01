package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.block.HarvestMethod$;
import mod.iceandshadow3.lib.common.LogicBlockMateria;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.WIdBlock;
import mod.iceandshadow3.lib.compat.block.WBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ABlockStairs extends StairsBlock implements IABlock {
	private final BLogicBlock logic;
	public ABlockStairs(LogicBlockMateria source) {
		super(
			//Note that we very much want an exception to be thrown here.
			() -> source.relative().unapply().get().typeDefault().exposeBS(),
			LogicToProperties$.MODULE$.toProperties(source)
		);
		logic = source;
	}

	@Nullable
	@Override
	public BLogicBlock getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WIdBlock id() {
		return logic.id();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		ABlockUtils.animateTick(logic, stateIn, worldIn, pos, rand);
	}
	@Nonnull
	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder cb) {
		return ABlockUtils.getDrops(logic, state, cb);
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		return logic.isToolClassEffective(HarvestMethod$.MODULE$.get(tool));
	}
}
