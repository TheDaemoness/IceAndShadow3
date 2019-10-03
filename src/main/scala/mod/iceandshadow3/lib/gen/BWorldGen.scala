package mod.iceandshadow3.lib.gen

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.compat.misc.WMutableBlockPos
import mod.iceandshadow3.lib.compat.world.BWorldRegionRef
import mod.iceandshadow3.lib.spatial.PairXZ
import mod.iceandshadow3.lib.util.collect.IMap2d

/** The new actual world generator for IaS3 dimensions.
	*
	* CONCURRENCY WARNING:
	* BWorldGen and all of its children need to be thread-safe.
	* That applies to all of their methods and fields.
	*/
abstract class BWorldGen(val seed: Long) {
	type RegionType <: BWorldGenRegionTerrain
	type ChunkType <: BWorldGenChunk
	protected def region(coord: PairXZ): RegionType
	protected def chunk(xFrom: Int, zFrom: Int, regions: IMap2d[RegionType]): ChunkType
	protected def column(xBlock: Int, zBlock: Int, region: ChunkType): BGeneratedColumn

	/** Write world gen info to the provided wrapped chunk.
		* WARNING: The passed chunk is not required to be thread-safe.
		*/
	final def write(chunk: BWorldRegionRef): Unit = {
		//WARNING: Assumes that chunks will not cross region boundaries.
		val genChunk = this.chunk(chunk.xFrom, chunk.zFrom, view)
		val wmbp = new WMutableBlockPos
		var xi: Int = chunk.xFrom
		while(xi < chunk.xMax) {
			var zi: Int = chunk.zFrom
			while(zi < chunk.zMax) {
				val col = column(xi, zi, genChunk)
				wmbp.set(xi, 0, zi)
				chunk(wmbp) = col.bedrock()
				var yi: Int = 1
				while(yi <= 255) {
					wmbp.set(xi, yi, zi)
					chunk(wmbp) = col(yi-1)
					yi += 1
				}
				zi += 1
			}
			xi += 1
		}
	}

	private val cache = CacheBuilder.newBuilder().
		concurrencyLevel(Runtime.getRuntime.availableProcessors()).
		initialCapacity(100).
		expireAfterWrite(30, TimeUnit.SECONDS).
		softValues().
		build(
			new CacheLoader[PairXZ, RegionType]{
				override def load(key: PairXZ) = region(key)
			}
		)
	private val view = new IMap2d[RegionType] {
		override def apply(xRegion: Int, zRegion: Int): RegionType = cache.get(PairXZ(xRegion, zRegion))
	}
}
