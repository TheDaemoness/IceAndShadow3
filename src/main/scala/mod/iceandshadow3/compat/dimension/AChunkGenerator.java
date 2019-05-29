package mod.iceandshadow3.compat.dimension;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import mod.iceandshadow3.basics.BDimension;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class AChunkGenerator implements IChunkGenerator {
	private final long seed;
	private final BDimension dim;
	private final ABiome abiome;
	private final BiomeProvider fauxBP = new BiomeProvider() {
		@Override
		public Biome getBiome(@Nonnull BlockPos pos, @Nullable Biome defaultBiome) {
			return abiome;
		}

		@Override
		public Biome[] getBiomes(int startX, int startZ, int xSize, int zSize) {
			final Biome[] retval = new Biome[xSize*zSize];
			Arrays.fill(retval, abiome);
			return retval;
		}

		@Override
		public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
			return getBiomes(x,z,width,length);
		}

		@Override
		public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
			return new HashSet<>(Collections.singletonList(abiome));
		}

		@Nullable
		@Override
		public BlockPos findBiomePosition(int x, int z, int range, @Nonnull List<Biome> biomes, @Nonnull Random random) {
			return biomes.contains(abiome) ? new BlockPos(x, 0, z) : null;
		}

		@Override
		public boolean hasStructure(@Nonnull Structure<?> structureIn) {
			return false;
		}

		@Override
		public Set<IBlockState> getSurfaceBlocks() {
			return Collections.emptySet();
		}
	};

	private final IChunkGenSettings fauxSettings = new IChunkGenSettings() {
		@Override public int getVillageDistance() { return 0; }
		@Override public int getVillageSeparation() { return 0; }
		@Override public int getOceanMonumentSpacing() { return 0; }
		@Override public int getOceanMonumentSeparation() { return 0; }
		@Override public int getStrongholdDistance() { return 0; }
		@Override public int getStrongholdCount() { return 0; }
		@Override public int getStrongholdSpread() { return 0; }
		@Override public int getBiomeFeatureDistance() { return 0; }
		@Override public int getBiomeFeatureSeparation() { return 0; }
		@Override public int func_204748_h() { return 0; }
		@Override public int func_211730_k() { return 0; }
		@Override public int func_204026_h() { return 0; }
		@Override public int func_211727_m() { return 0; }
		@Override public int getEndCityDistance() { return 0; }
		@Override public int getEndCitySeparation() { return 0; }
		@Override public int getMansionDistance() { return 0; }
		@Override public int getMansionSeparation() { return 0; }
		@Nonnull
		@Override public IBlockState getDefaultBlock() { return dim.defaultLand().exposeState(); }
		@Nonnull
		@Override public IBlockState getDefaultFluid() { return dim.defaultSea().exposeState(); }
	};

	AChunkGenerator(World w, BDimension dim, ABiome dimbiome) {
		this.seed = w.getSeed();
		this.dim = dim;
		this.abiome = dimbiome;
	}

	@Override
	public void makeBase(@Nonnull IChunk chunk) {
		chunk.setBiomes(fauxBP.getBiomes(0, 0, 16, 16));
		//TODO: Extremely temporary worldgen.
		for(BlockPos bp : BlockPos.getAllInBox(0, dim.seaLevel(), 0, 16, dim.seaLevel(), 16)) {
			chunk.setBlockState(bp, Blocks.BEDROCK.getDefaultState(), false);
		}
		chunk.setStatus(ChunkStatus.FULLCHUNK);
	}

	@Override
	public void carve(@Nonnull WorldGenRegion region, @Nonnull GenerationStage.Carving carvingStage) {
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
	public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType a, @Nonnull BlockPos b) {
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public BlockPos findNearestStructure(
		@Nonnull World w, @Nonnull String name, @Nonnull BlockPos pos, int radius, boolean flag
	) {
		//TODO: We *can* actually allow this to work.
		return null;
	}

	@Nonnull
	@Override
	public IChunkGenSettings getSettings() {
		return fauxSettings;
	}

	@Override
	public int spawnMobs(@Nonnull World worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs) {
		return 0;
	}

	@Nonnull
	@Override
	public BiomeProvider getBiomeProvider() {
		return fauxBP;
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

	@Nonnull
	@Override
	public Long2ObjectMap<LongSet> getStructurePositionToReferenceMap(@Nonnull Structure structureIn) {
		return new Long2ObjectArrayMap<>();
	}

	@Nonnull
	@Override
	public Long2ObjectMap<StructureStart> getStructureReferenceToStartMap(@Nonnull Structure structureIn) {
		return new Long2ObjectArrayMap<>();
	}

	@Nullable
	@Override
	public IFeatureConfig getStructureConfig(@Nonnull Biome biomeIn, @Nonnull Structure structureIn) {
		return null;
	}

	@Override
	public boolean hasStructure(@Nonnull Biome biomeIn, @Nonnull Structure structureIn) {
		return false;
	}
}
