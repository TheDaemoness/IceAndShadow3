package mod.iceandshadow3.gen

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.compat.block.`type`.BBlockType
import mod.iceandshadow3.lib.compat.world.BRegionRef
import mod.iceandshadow3.lib.spatial.{Cells, PairXZ}
import mod.iceandshadow3.lib.util.collect.IMap2d

object BWorldGen {
	final val regionWidth = 32 //2x chunk width, which should be aligned with chunks under Cells logic.
}

/** The new actual world generator for IaS3 dimensions.
	*
	* CONCURRENCY WARNING:
	* BWorldGen and all of its children need to be thread-safe.
	* That applies to all of their methods and fields.
	*/
abstract class BWorldGen(val seed: Long) {
	type ColumnDataType
	type RegionDataType
	type RegionInterpretType
	protected def region(xFrom: Int, zFrom: Int): RegionDataType
	protected def interpret(xRegion: Int, zRegion: Int, regions: IMap2d[RegionDataType]): RegionInterpretType
	protected def column(xBlock: Int, zBlock: Int, region: RegionInterpretType): ColumnDataType
	protected def block(column: ColumnDataType, y: Int): BBlockType

	final def write(chunk: BRegionRef): Unit = {
		//WARNING: Assumes that chunks will not cross region boundaries.
		val region = interpret(
			Cells.rescale(chunk.xFrom, BWorldGen.regionWidth),
			Cells.rescale(chunk.zFrom, BWorldGen.regionWidth),
			view
		)
		var xi: Int = chunk.xFrom;
		while(xi <= chunk.xMax) {
			var zi: Int = chunk.zFrom;
			while(zi <= chunk.zMax) {
				val column = column(xi, zi, region)
				var yi: Int = 0
				while(yi <= 255) {
					chunk(xi, yi, zi) = block(column, yi)
					yi += 1
				}
				zi += 1
			}
			xi += 1
		}
	}

	private val cache = CacheBuilder.newBuilder().
		maximumSize(36).
		expireAfterAccess(1, TimeUnit.MINUTES).
		build(
			new CacheLoader[PairXZ, RegionDataType]{
				override def load(key: PairXZ) = region(
					key.x,
					key.z
				)
			}
		)
	private val view = new IMap2d[RegionDataType] {
		override def apply(x: Int, z: Int): RegionDataType = cache.get(PairXZ(x, z))
	}
}
