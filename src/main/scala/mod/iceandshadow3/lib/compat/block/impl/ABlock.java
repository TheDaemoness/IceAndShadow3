package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.block.HarvestMethod$;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.WId;
import mod.iceandshadow3.lib.compat.block.*;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.entity.CNVEntity$;
import mod.iceandshadow3.lib.compat.entity.WEntity;
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer;
import mod.iceandshadow3.lib.compat.item.WItemStackOwned;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

//NOTE: The deprecation suppression is here because the methods are supposed to be called indirectly via IBlockState.
//Overriding them is fine.

@SuppressWarnings("deprecation")
final public class ABlock extends Block
implements IABlock, IShearable {
	
	private final BLogicBlock logic;
	private final BlockRenderLayer layer;
	private final VoxelShape defaultShape;
	private final ResourceLocation lootTable;
	private final StateContainer<net.minecraft.block.Block, BlockState> realContainer;
	@SuppressWarnings("unchecked")
	public ABlock(BLogicBlock blocklogic) {
		super(LogicToProperties$.MODULE$.toProperties(blocklogic));
		logic = blocklogic;
		if(logic.materia().transparent()) layer = BlockRenderLayer.TRANSLUCENT;
		else if(!logic.areSurfacesFull()) layer = BlockRenderLayer.CUTOUT_MIPPED;
		else layer = BlockRenderLayer.SOLID;

		defaultShape = CNVBlockShape$.MODULE$.toVoxelShape(logic);
		lootTable = new ResourceLocation(IaS3.MODID,"blocks/"+logic.name());

		//State container init happens too early for IaS3. We need to make our own.
		final StateContainer.Builder<net.minecraft.block.Block, BlockState> builder =
			new StateContainer.Builder<>(this);
		final BinderBlockVar$ binder = BinderBlockVar$.MODULE$;
		for(BVarBlock<?> bbv : logic.variables().asJava()) {
			builder.add(binder.apply(bbv).expose());
		}
		realContainer = builder.create(BlockState::new);
		BlockState bbs = this.getStateContainer().getBaseState();
		for(BVarBlock bbv : logic.variables().asJava()) {
			bbs = binder.get(bbv).addTo(bbs, bbv.defaultVal());
		}
		setDefaultState(bbs);
	}

	@Nonnull
	@Override
	public BLogicBlock getLogic() {
		return logic;
	}

	@Override
	@Nonnull
	public BlockState mirror(@Nonnull BlockState state, Mirror mirrorIn) {
		//TODO: Support at the BBlockVar level.
		return super.mirror(state, mirrorIn);
	}

	@Override
	@Nonnull
	public BlockState rotate(@Nonnull BlockState state, Rotation rot) {
		//TODO: Support at the BBlockVar level.
		return super.rotate(state, rot);
	}

	@Override
	@Nonnull
	public StateContainer<net.minecraft.block.Block, BlockState> getStateContainer() {
		return realContainer;
	}

	protected void fillStateContainer(StateContainer.Builder<net.minecraft.block.Block, BlockState> builder) {
		//No-op. DON'T PUT STUFF HERE!
	}

	@Override
	public boolean onBlockActivated(
		BlockState state, World worldIn, BlockPos pos,
		PlayerEntity player, Hand handIn, BlockRayTraceResult rt
	) {
		return logic.onUsed(
			new WBlockRef(worldIn, pos, state),
			new WItemStackOwned<>(player.getHeldItem(handIn), CNVEntity.wrap(player))
		);
	}

	@Nullable
	@Override
	public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
		return logic.container(new WBlockRef(worldIn, pos, state)).expose();
	}

	@Nonnull
	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder cb) {
		return ABlockUtils.getDrops(logic, state, cb);
	}

	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return layer;
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		return logic.isToolClassEffective(HarvestMethod$.MODULE$.get(tool));
	}

	@Override
	public int getHarvestLevel(BlockState state) {
		return logic.materia().harvestLevel();
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
		logic.isToolClassEffective(HarvestMethod$.MODULE$.SHEAR()); //TODO: Drops.
		return Collections.emptyList();
	}

	@Override
	public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
		return logic.harvestXP(new WBlockView(world, pos, state), silktouch > 0);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		ABlockUtils.animateTick(logic, stateIn, worldIn, pos, rand);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isSideInvisible(BlockState state, BlockState abs, Direction side) {
		return !logic.isDiscrete() && logic.materia().transparent() && abs.getBlock() == this ||
			super.isSideInvisible(state, abs, side);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return defaultShape;
	}


	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return logic.canStayAt(new WBlockView(worldIn, pos), false);
	}

	@Nonnull
	@Override
	public BlockState updatePostPlacement(
		@Nonnull BlockState stateIn, Direction facing, BlockState facingState,
		IWorld worldIn, BlockPos currentPos, BlockPos facingPos
	) {
		//TODO: BlockType on breakage.
		if(!logic.canStayAt(new WBlockView(worldIn, currentPos), true)) {
			return Blocks.AIR.getDefaultState();
		} else {
			final WBlockRef us = new WBlockRef(worldIn, currentPos, stateIn);
			final WBlockRef them = new WBlockRef(worldIn, facingPos, facingState);
			final WBlockState nova = logic.onNeighborChanged(us, them);
			return (nova == null ? us : nova).exposeBS();
		}
	}

	@Override
	public void onReplaced(
			BlockState state,
			@Nonnull World worldIn,
			@Nonnull BlockPos pos,
			@Nonnull BlockState newState,
			boolean isMoving
	) {
		if(state.getBlock() != newState.getBlock()) {
			logic.onReplaced(
				new WBlockState(state),
				new WBlockRef(worldIn, pos, newState),
				isMoving
			);
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return logic.toPlace(new WBlockState(this.getDefaultState()), new WUsagePlace(context)).exposeBS();
	}

	@Override
	public void onEntityCollision(
		BlockState state,
		World worldIn,
		BlockPos pos,
		net.minecraft.entity.Entity entityIn
	) {
		final BiConsumer<WBlockRef, WEntity> handler = logic.handlerEntityInside();
		if(handler != null) handler.accept(new WBlockRef(worldIn, pos, state), CNVEntity$.MODULE$.wrap(entityIn));
	}

	@Override
	public void randomTick(
			@Nonnull BlockState state,
			@Nonnull World worldIn,
			@Nonnull BlockPos pos,
			@Nonnull Random random
	) {
		final WBlockRef ref = new WBlockRef(worldIn, pos, state);
		if(logic.onRandomTick(ref, random)) logic.onUpdateTick(ref, random);
	}

	@Override
	public void onBlockAdded(BlockState us, World w, BlockPos pos, BlockState old, boolean moving) {
		logic.onAdded(new WBlockRef(w, pos, us), new WBlockState(old), moving);
	}

	@Override
	public int tickRate(IWorldReader p_149738_1_) {
		return super.tickRate(p_149738_1_);
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		logic.onUpdateTick(new WBlockRef(worldIn,pos, state), random);
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return logic.randomlyUpdates().fold(() -> Boolean.FALSE, fn -> (Boolean)fn.apply(new WBlockState(state)));
	}

	@Nonnull
	@Override
	public WId id() {
		return logic.id();
	}
}

