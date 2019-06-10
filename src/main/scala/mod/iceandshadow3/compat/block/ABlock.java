package mod.iceandshadow3.compat.block;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.block.HarvestMethod$;
import mod.iceandshadow3.basics.util.LogicPair;
import mod.iceandshadow3.compat.ILogicBlockProvider;
import mod.iceandshadow3.compat.entity.CNVEntity$;
import mod.iceandshadow3.compat.world.WWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//NOTE: The deprecation suppression is here because the methods are supposed to be called indirectly via IBlockState.
//Overriding them is fine.

@SuppressWarnings("deprecation")
public class ABlock extends Block implements ILogicBlockProvider, IShearable {
	
	private final BLogicBlock logic;
	private final int variant;
	private final BlockRenderLayer layer;

	@Nonnull
	@Override
	public LogicPair<BLogicBlock> getLogicPair() {
		return new LogicPair<>(logic, variant);
	}

	private final VoxelShape defaultShape;

	public ABlock(BLogicBlock blocklogic, int variant) {
		super(((BCompatLogicBlock)blocklogic).toBlockProperties(variant));
		logic = blocklogic;
		this.setRegistryName(IaS3.MODID, logic.getName(variant));
		this.variant = variant;
		if(logic.getMateria().isTransparent()) layer = BlockRenderLayer.TRANSLUCENT;
		else if(!logic.areSurfacesFull(variant)) layer = BlockRenderLayer.CUTOUT_MIPPED;
		else layer = BlockRenderLayer.SOLID;

		defaultShape = CNVBlockShape$.MODULE$.toVoxelShape(logic);
	}

	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return layer;
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		return logic.isToolClassEffective(variant, HarvestMethod$.MODULE$.get(tool));
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
		logic.isToolClassEffective(variant, HarvestMethod$.MODULE$.SHEAR()); //TODO: Drops.
		return Collections.emptyList();
	}



	@Nonnull
	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState bs, @Nonnull LootContext.Builder loottable) {
		//TODO: Currently no-op.
		return super.getDrops(bs, loottable);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		logic.clientSideTick(variant, new WWorld(worldIn), new WBlockView(worldIn, pos, stateIn), rand);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isSideInvisible(BlockState state, BlockState abs, Direction side) {
		return !logic.isDiscrete() && logic.getMateria().isTransparent() && abs.getBlock() == this ||
			super.isSideInvisible(state, abs, side);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return defaultShape;
	}


	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return logic.canBeAt(variant, new WBlockView(worldIn, pos), false);
	}

	@Nonnull
	@Override
	public BlockState updatePostPlacement(
		@Nonnull BlockState stateIn, Direction facing, BlockState facingState,
		IWorld worldIn, BlockPos currentPos, BlockPos facingPos
	) {
		//TODO: BlockType on breakage.
		if(!logic.canBeAt(variant, new WBlockView(worldIn, currentPos), true)) return Blocks.AIR.getDefaultState();
		else {
			WBlockRef us = new WBlockRef(worldIn, currentPos, stateIn);
			logic.onNeighborChanged(variant, us, new WBlockRef(worldIn, facingPos, facingState));
			return us.exposeBS();
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		logic.onInside(variant, new WBlockRef(worldIn, pos, state), CNVEntity$.MODULE$.wrap(entityIn));
	}
}

