package mod.iceandshadow3.lib.base

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.compat.world.BRegionRef
import mod.iceandshadow3.lib.spatial.PairXZ
import mod.iceandshadow3.lib.util.collect.IMap2d

/** The new actual world generator for IaS3 dimensions.
	*
	* CONCURRENCY WARNING:
	* BWorldGen and all of its children need to be thread-safe.
	* That applies to all of their methods and fields.
	*/
object BWorldGen {
	final val widthRegion = 32
	final val widthChunk = 16
	def toRegion(blockCoord: Int): Int = (blockCoord + 16) >> 5
	def toEdge(regionCoord: Int): Int = regionCoord*32 - 16
}
abstract class BWorldGen(val seed: Long) {
	type RegionType <: BWorldGenRegion
	type ChunkType <: BWorldGenChunk
	protected def region(coord: PairXZ): RegionType
	protected def chunk(xFrom: Int, zFrom: Int, regions: IMap2d[RegionType]): ChunkType
	protected def column(xBlock: Int, zBlock: Int, region: ChunkType): BWorldGenColumn

	/** Write world gen info to the provided wrapped chunk.
		* WARNING: The passed chunk is not required to be thread-safe.
		*/
	final def write(chunk: BRegionRef): Unit = {
		//WARNING: Assumes that chunks will not cross region boundaries.
		val genChunk = this.chunk(chunk.xFrom, chunk.zFrom, view)
		var xi: Int = chunk.xFrom
		while(xi <= chunk.xMax) {
			var zi: Int = chunk.zFrom
			while(zi <= chunk.zMax) {
				val col = column(xi, zi, genChunk)
				chunk(xi, 0, zi) = col.bedrock()
				var yi: Int = 1
				while(yi <= 255) {
					chunk(xi, yi, zi) = col(yi-1)
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
