package mod.iceandshadow3.compat.dimension;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BDimension;
import mod.iceandshadow3.compat.block.type.BBlockType;
import mod.iceandshadow3.gen.BChunkSource;
import mod.iceandshadow3.gen.BWorldSource;
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
	private final BWorldSource realworldgen;

	AChunkGenerator(World w, BDimension dim, ABiome dimbiome) {
		super(w, new ABiomeProvider(dimbiome), AGenerationSettings.instance);
		this.seed = w.getSeed();
		this.dim = dim;
		this.realworldgen = dim.getWorldSource(seed);
	}

	// WORLDGENERATION LOGIC HERE!

	@Override
	public void generateSurface(@Nonnull IChunk chunk) {
		BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
		ChunkPos cp = chunk.getPos();
		int xFirst = cp.getXStart(), zFirst = cp.getZStart();
		int xLast = cp.getXEnd(), zLast = cp.getZEnd();
		try {
			BChunkSource bcs = realworldgen.getTerrainChunk(
				xFirst, zFirst,
				xLast - xFirst + 1, zLast - zFirst + 1
			);
			for (int xit = xFirst; xit <= xLast; ++xit) {
				for (int zit = zFirst; zit <= zLast; ++zit) {
					final BBlockType[] column = bcs.getColumn(xit, zit);
					for (int yit = 0; yit < 256; ++yit) {
						mbp.setPos(xit, yit, zit);
						BBlockType bt = column[yit];
						chunk.setBlockState(mbp, (bt != null ? bt : bcs.getDefaultAir(yit)).state(), false);
					}
				}
			}
		} catch(Exception e) {
			IaS3.bug(e, "Worldgen failure on "+dim.name());
		}
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
	public void func_222537_b(@Nonnull IWorld iWorld, @Nonnull IChunk iChunk) {
		//Seems to be used for noise. No-op.
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
