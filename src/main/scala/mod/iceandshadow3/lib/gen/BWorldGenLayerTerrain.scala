package mod.iceandshadow3.lib.gen

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.data.BVar
import mod.iceandshadow3.lib.spatial.PairXZ
import mod.iceandshadow3.lib.util.collect.{FixedMap2d, IMap2d}

object BWorldGenLayerTerrain {
	val varHeight = new BVar[Float]("height") {
		override def defaultVal = 0f
	}
	private final val widthPow = 5
	private final val width = 1 << widthPow
	private final val widthHalf = width >> 1
	private def toEdge(remapped: Int): Int = remapped*width + widthHalf
	private def remap(blockCoord: Int): Int = (blockCoord-widthHalf) >> widthPow
	private class Region[Column <: BWorldGenColumnFn](
		xFrom: Int,
		zFrom: Int,
		gen: (Int, Int, Int) => (Int, Int) => Column
	) extends IMap2d[Column]{
		private val map = new FixedMap2d[Column](
			xFrom, zFrom, width, width, gen(xFrom, zFrom, width)
		)
		override def apply(x: Int, z: Int) = map(x, z)
	}
}
abstract class BWorldGenLayerTerrain[Column <: BWorldGenColumnFn] extends TWorldGenLayer[Column] {
	private type Region = BWorldGenLayerTerrain.Region[Column]
	protected def newGenerator(xFrom: Int, zFrom: Int, width: Int): (Int, Int) => Column
	private val cache = CacheBuilder.newBuilder().
		concurrencyLevel(Runtime.getRuntime.availableProcessors()).
		initialCapacity(100).
		expireAfterWrite(30, TimeUnit.SECONDS).
		softValues().
		build(
			new CacheLoader[PairXZ, Region]{
				override def load(key: PairXZ) = new Region(
					BWorldGenLayerTerrain.toEdge(key.x),
					BWorldGenLayerTerrain.toEdge(key.z),
					newGenerator
				)
			}
		)
	import BWorldGenLayerTerrain.remap
	private def getAt(xRemap: Int, zRemap: Int): Region = cache.get(PairXZ(xRemap, zRemap))
	final override def getForChunk(where: WChunk) = getAt(remap(where.xFrom), remap(where.zFrom))
	final override def apply(x: Int, z: Int) = getAt(remap(x), remap(z)).apply(x, z)
}
