package mod.iceandshadow3.lib.gen

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.spatial.PairXZ

class WorldGenLayerTerrain[RegionType <: BWorldGenRegionTerrain](factory: PairXZ => RegionType)
extends TWorldGenLayer[RegionType] {
	private val cache = CacheBuilder.newBuilder().
		concurrencyLevel(Runtime.getRuntime.availableProcessors()).
		initialCapacity(100).
		expireAfterWrite(30, TimeUnit.SECONDS).
		softValues().
		build(
			new CacheLoader[PairXZ, Seq[RegionType]]{
				override def load(key: PairXZ) = List(factory(key))
			}
		)

	override protected def remapCoord(blockCoord: Int) = BWorldGenRegionTerrain.coord(blockCoord)
	override protected def getAt(xRemapped: Int, zRemapped: Int) = cache.get(PairXZ(xRemapped, zRemapped))

	override def getForChunk(where: WChunk) = getAt(remapCoord(where.xFrom), remapCoord(where.zFrom))
}
