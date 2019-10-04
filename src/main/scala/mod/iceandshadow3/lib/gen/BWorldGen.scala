package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.misc.WMutableBlockPos
import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.spatial.RandomXZ

import scala.collection.immutable

/** The new actual world generator for IaS3 dimensions.
	*
	* CONCURRENCY WARNING:
	* write() is called in a multithreaded context. Everything passed to WorldGen should be thread-safe.
	*/
abstract class BWorldGen(
	val seed: Long,
	defaultState: Int => WBlockState,
) {
	protected val layers: Seq[TWorldGenLayer[BWorldGenRegion]]
	/** Write world gen info to the provided wrapped chunk.
		*/

	private def regions(chunk: WChunk): Seq[BWorldGenRegion] = {
		val builder = immutable.Seq.newBuilder[BWorldGenRegion]
		var idx = 0
		while(idx < layers.length) {
			builder.addAll(layers(idx).getForChunk(chunk))
			idx += 1
		}
		builder.result()
	}
	final def write(chunk: WChunk): Unit = {
		val wBlockPos = new WMutableBlockPos
		val column = new WorldGenColumn(
			chunk.xFrom, chunk.zFrom,
			new RandomXZ(seed, 31927, chunk.xMax, chunk.zMax),
			defaultState
		)
		val regions = this.regions(chunk)
		while(column.x <= chunk.xMax) {
			while(column.z <= chunk.zMax) {
				column.reset()
				var idxRegion = 0
				while(idxRegion < regions.length) {
					val region = regions(idxRegion)
					if(region != null) region(column)(column)
					idxRegion += 1
				}
				var yi = 0
				while(yi < column.length) {
					wBlockPos.set(column, yi)
					chunk(wBlockPos) = column(yi)
					yi += 1
				}
				column.z += 1
			}
			column.z = chunk.zFrom
			column.x += 1
		}
	}
}
