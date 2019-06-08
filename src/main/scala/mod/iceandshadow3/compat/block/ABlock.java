package mod.iceandshadow3.compat.block;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.block.BlockShape;
import mod.iceandshadow3.basics.block.BlockSides;
import mod.iceandshadow3.basics.block.HarvestMethod$;
import mod.iceandshadow3.basics.util.LogicPair;
import mod.iceandshadow3.compat.ILogicBlockProvider;
import mod.iceandshadow3.compat.entity.CNVEntity$;
import mod.iceandshadow3.compat.item.WRefItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

//NOTE: The deprecation warnings in here are because the methods are supposed to be called indirectly via IBlockState.
//Overriding them is fine.

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
	public boolean isToolEffective(IBlockState state, ToolType tool) {
		return logic.isToolClassEffective(variant, HarvestMethod$.MODULE$.get(tool));
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
		logic.isToolClassEffective(variant, HarvestMethod$.MODULE$.SHEAR()); //TODO: Drops.
		return Collections.emptyList();
	}
	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
		final WRefItem[] overrides = logic.harvestOverride(variant, new WRefBlock(world, pos, state), fortune);
		if(overrides != null) {
			drops.clear();
			for(WRefItem item : overrides) {
				if(item == null || item.isEmpty()) continue;
				drops.add(item.exposeItems());
			}
		} else super.getDrops(state, drops, world, pos, fortune);
	}

	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return defaultShape;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return logic.shape().sides().isFullCube();
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		//TODO: More dynamic EnumFacing -> BlockSides val mapping.
		final BlockShape shape = logic.shape();
		final BlockSides sides = shape.sides();
		ESideType sideType;
		switch(face) {
			case DOWN: sideType = sides.bottom(); break;
			case UP: sideType = sides.top(); break;
			default: sideType = sides.front(); break;
		}
		return sideType.expose(face, shape.isPole());
	}

	@Override
	public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
		return logic.canBeAt(new WRefBlock(worldIn, pos), false);
	}

	@Override
	public IBlockState updatePostPlacement(
		@Nonnull IBlockState stateIn, EnumFacing facing, IBlockState facingState,
		IWorld worldIn, BlockPos currentPos, BlockPos facingPos
	) {
		//TODO: BlockType on breakage.
		return !logic.canBeAt(new WRefBlock(worldIn, currentPos), true) ? Blocks.AIR.getDefaultState() :
			super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		logic.onInside(new WRefBlock(worldIn, pos, state), CNVEntity$.MODULE$.wrap(entityIn));
	}

	@Override
	public int getExpDrop(IBlockState state, IWorldReader world, BlockPos pos, int fortune) {
		return logic.harvestXP(variant);
	}
}

