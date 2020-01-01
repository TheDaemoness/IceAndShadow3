package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.block.HarvestMethod$;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.WId;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
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

public class ABlockWall extends WallBlock implements IABlock {
	private final BLogicBlock logic;
	public ABlockWall(BLogicBlock blb) {
		super(LogicToProperties$.MODULE$.toProperties(blb));
		logic = blb;
	}

	@Override
	public boolean isIn(Tag<net.minecraft.block.Block> tag) {
		return super.isIn(tag) || tag == BlockTags.WALLS;
	}

	@Nullable
	@Override
	public BLogicBlock getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WId id() {
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
