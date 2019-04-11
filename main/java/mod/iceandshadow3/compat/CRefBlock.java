package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.Damage;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

//TODO: There's a LOT missing here. It's barely usable.
public class CRefBlock extends BCRefWorld implements IEffectSource {
	private abstract class Impl {
		final BlockPos pos;
		Impl(BlockPos position) {pos = position;};
		public abstract IBlockState getBS();
		public abstract IBlockAccess getIBA();
		public abstract World getWorld();
	}
	private class ImplChunk extends Impl {
		final Chunk chunk;
		ImplChunk(Chunk ck, BlockPos position) {
			super(position);
			chunk = ck;
		}
		public IBlockState getBS() {
			return chunk.getBlockState(pos);
		}
		public IBlockAccess getIBA() {
			return chunk.getWorld();
		}
		public World getWorld() {
			return chunk.getWorld();
		}
	}
	private class ImplStupid extends Impl {
		final IBlockAccess notaworld;
		final World maybeaworld;
		ImplStupid(IBlockAccess iba, BlockPos position) {
			super(position);
			notaworld = iba;
			maybeaworld = (iba instanceof World)?(World)iba:null;
		}
		public IBlockState getBS() {
			return notaworld.getBlockState(pos);
		}
		public IBlockAccess getIBA() {
			return notaworld;
		}
		public World getWorld() {
			return maybeaworld;
		}
	}
	
	private final Impl impl;
	@Override
	protected World getWorld() {
		return impl.getWorld();
	}
	
	CRefBlock(World w, Vec3 v) {
		impl = new ImplChunk(
			w.getChunkFromChunkCoords(v.xChunk(), v.zChunk()),
			new BlockPos((int)v.xBlock(), (short)Math.min(v.yBlock(), 255), (int)v.zBlock())
		);
	}
	CRefBlock(World w, BlockPos p) {
		impl = new ImplChunk(
			w.getChunkFromChunkCoords(p.getX() >> 4, p.getZ() >> 4),
			p
		);
	}
	CRefBlock(IBlockAccess notaworld, BlockPos bp) {
		impl = new ImplStupid(notaworld, bp);
			
	}
	public float getHardness() {
		final World w = impl.getWorld();
		if(w == null) return Float.NaN;
		return impl.getBS().getBlockHardness(w, impl.pos);
	}
	public int getOpacity() {
		return impl.getBS().getLightOpacity(impl.getIBA(), impl.pos);
	}
	public int getLuma() {
		return impl.getBS().getLightValue(impl.getIBA(), impl.pos);
	}
	@Override
	public ITextComponent getNameTextComponent() {
		return new TextComponentString(impl.getBS().getBlock().getLocalizedName());
	}
	@Override
	public Damage getAttack() {
		return null;
	}
}
