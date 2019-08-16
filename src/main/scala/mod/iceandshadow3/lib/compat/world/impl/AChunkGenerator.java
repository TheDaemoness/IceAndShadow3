package mod.iceandshadow3.lib.compat.world.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BDimension;
import mod.iceandshadow3.lib.base.BWorldGen;
import mod.iceandshadow3.lib.compat.block.WBlockRef;
import mod.iceandshadow3.lib.compat.block.type.BBlockType;
import mod.iceandshadow3.lib.compat.world.BRegionRef;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class AChunkGenerator extends ChunkGenerator<AGenerationSettings> {
	private final long seed;
	private final BDimension dim;
	private final BWorldGen realworldgen;

	AChunkGenerator(World w, BDimension dim, ABiome dimbiome) {
		super(w, new ABiomeProvider(dimbiome), AGenerationSettings.instance);
		this.seed = w.getSeed();
		this.dim = dim;
		this.realworldgen = dim.getWorldGen(seed);
	}

	// WORLDGENERATION LOGIC HERE!

	@Override
	public void makeBase(@Nonnull IWorld iWorld, @Nonnull IChunk chunk) {
		ChunkPos cp = chunk.getPos();
		final int
				xFirst = cp.getXStart(),
				zFirst = cp.getZStart(),
				xLast = cp.getXEnd(),
				zLast = cp.getZEnd();
		try {
			realworldgen.write(new BRegionRef(xFirst, zFirst, xLast, zLast) {
				final private BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
				@Override
				public void update(int xBlock, int yBlock, int zBlock, BBlockType newtype) {
					mbp.setPos(xBlock, yBlock, zBlock);
					chunk.setBlockState(mbp, newtype.state(), false);
				}

				@Override
				public WBlockRef apply(int xBlock, int yBlock, int zBlock) {
					final BlockPos pos = new BlockPos(xBlock, yBlock, zBlock);
					return new WBlockRef(chunk, pos, chunk.getBlockState(pos));
				}
			});
		} catch(Exception e) {
			IaS3.bug(e, "Worldgen failure on "+dim.name());
		}
	}

	@Override
	public void generateSurface(@Nonnull IChunk chunk) {
		//No-op.
	}

	@Override
	public void decorate(@Nonnull WorldGenRegion region) {
		//No-op.
	}

	@Override
	public void spawnMobs(@Nonnull WorldGenRegion region) {
		//No-op.
	}

	@Nonnull
	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EntityClassification a, @Nonnull BlockPos b) {
		return Collections.emptyList();
	}

	@Override
	public int func_222529_a(int i, int i1, @Nonnull Heightmap.Type type) {
		//TODO: Determine what this does.
		return dim.peakLevel();
	}

	@Nullable
	@Override
	public BlockPos findNearestStructure(
		@Nonnull World w, @Nonnull String name, @Nonnull BlockPos pos, int radius, boolean flag
	) {
		//TODO: We *can* actually allow this to work.
		return null;
	}

	@Override
	public long getSeed() {
		return seed;
	}

	@Override
	public int getGroundHeight() {
		return dim.seaLevel();
	}

	@Override
	public int getMaxHeight() {
		return dim.peakLevel();
	}

	@Override
	public boolean hasStructure(@Nonnull Biome biomeIn, @Nonnull Structure structureIn) {
		return false;
	}
}
