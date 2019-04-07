package mod.iceandshadow3.compat;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CRefBlock {
	final Chunk chunk; //All well, when in Rome.
	final BlockPos pos;
	
	protected IBlockState getBS() {
		return chunk.getBlockState(pos);
	}
	protected World getWorld() {
		return chunk.getWorld();
	}
	
	public CRefBlock(World w, Vec3 v) {
		chunk = w.getChunkFromChunkCoords(v.xChunk(), v.zChunk());
		pos = new BlockPos((int)v.xBlock(), (short)Math.min(v.yBlock(), 255), (int)v.zBlock());
	}
	public CRefBlock(World w, BlockPos p) {
		chunk = w.getChunkFromChunkCoords(p.getX() >> 4, p.getZ() >> 4);
		pos = p;
	}
	public float getHardness() {
		return getBS().getBlockHardness(getWorld(), pos);
	}
	public int getOpacity() {
		return getBS().getLightOpacity(getWorld(), pos);
	}
	public int getLuma() {
		return getBS().getLightValue(getWorld(), pos);
	}
}
