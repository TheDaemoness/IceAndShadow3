package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.block.HandlerComparator;
import mod.iceandshadow3.lib.block.HarvestMethod$;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.id.WId;
import mod.iceandshadow3.lib.compat.block.*;
import mod.iceandshadow3.lib.compat.entity.CNVEntity;
import mod.iceandshadow3.lib.compat.entity.CNVEntity$;
import mod.iceandshadow3.lib.compat.entity.WEntity;
import mod.iceandshadow3.lib.compat.item.WItemStackOwned;
import mod.iceandshadow3.lib.util.E3vl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
	private final HandlerComparator handlerComparator;
	private final BlockShapes shapes;
	private final StateContainer<net.minecraft.block.Block, BlockState> realContainer;
	@SuppressWarnings("unchecked")
	public ABlock(BLogicBlock blocklogic) {
		super(LogicToProperties$.MODULE$.toProperties(blocklogic));
		logic = blocklogic;
		shapes = logic.shape();
		//State container init happens too early for IaS3. We need to make our own.
		final StateContainer.Builder<net.minecraft.block.Block, BlockState> builder =
			new StateContainer.Builder<>(this);
		final BinderBlockVar$ binder = BinderBlockVar$.MODULE$;
		for(VarBlock<?> bbv : logic.variables().asJava()) {
			builder.add(binder.apply(bbv).expose());
		}
		realContainer = builder.create(BlockState::new);
		BlockState bbs = this.getStateContainer().getBaseState();
		for(VarBlock bbv : logic.variables().asJava()) {
			bbs = binder.get(bbv).addTo(bbs, bbv.defaultVal());
		}
		setDefaultState(bbs);
		handlerComparator = logic.handlerComparator();
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

	@Nonnull
	@Override
	public ActionResultType func_225533_a_(
			BlockState state, World worldIn, BlockPos pos,
			PlayerEntity player, Hand handIn, BlockRayTraceResult rt
	) {
		return logic.onUsed(
			new WBlockRef(worldIn, pos, state),
			new WItemStackOwned<>(player.getHeldItem(handIn), CNVEntity.wrap(player))
		).remap(ActionResultType.SUCCESS, ActionResultType.PASS, ActionResultType.FAIL);
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

	@Override
	public OffsetType getOffsetType() {
		return super.getOffsetType();
	}

	@Override
	public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
		final E3vl result = ABlockUtils.canHarvestBlock(logic, state, world, pos, player);
		return result.isNeutral() ? super.canHarvestBlock(state, world, pos, player) : result.isTrue();
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		return logic.canDigFast(HarvestMethod$.MODULE$.get(tool));
	}

	@Override
	public int getHarvestLevel(BlockState state) {
		return logic.materia().harvestLevel();
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
		logic.canDigFast(HarvestMethod$.MODULE$.SHEAR()); //TODO: Drops.
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
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext context) {
		return shapes.apply(new WBlockView(view, pos, state));
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
	public void func_225542_b_(
			@Nonnull BlockState state,
			@Nonnull ServerWorld worldIn,
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
	public void func_225534_a_(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
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

	@Override
	public boolean hasTileEntity(BlockState state) {
		return logic.tileEntity().isDefined();
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return BinderTileEntity.create(logic.tileEntity());
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		final E3vl shortcut = handlerComparator.hasValue();
		if(shortcut.isNeutral()) {
			return handlerComparator.hasValue(new WBlockState(state));
		} else return shortcut.isTrue();
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return handlerComparator.value(new WBlockRef(worldIn, pos, blockState));
	}
}

