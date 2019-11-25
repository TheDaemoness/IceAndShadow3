package mod.iceandshadow3.lib.gen

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.data.BVar
import mod.iceandshadow3.lib.spatial.{IMap2d, IRegion2d, TupleXZ}
import mod.iceandshadow3.lib.util.collect.{FixedMap2d, MutableMap2d}

import scala.reflect.ClassTag

object BWorldGenLayerTerrain {
	val varHeight = new BVar[Float]("height") {
		override def defaultVal = 0f
	}
	private final val widthPow = 5
	private final val width = 1 << widthPow
	private final val widthHalf = width >> 1
	private def toEdge(remapped: Int): Int = remapped*width + widthHalf
	private def remap(blockCoord: Int): Int = (blockCoord-widthHalf) >> widthPow

	private class Region[Column <: TWorldGenColumnFn: ClassTag](
		xFrom_ : Int,
		zFrom_ : Int,
		gen: (Int, Int, Int) => (Int, Int) => Column
	) extends IMap2d[Column] with IRegion2d {
		private val map = new FixedMap2d[Column](
			xFrom_, zFrom_, width, width, gen(xFrom_, zFrom_, width)
		)
		def xFrom = map.xFrom
		def zFrom = map.zFrom
		override def xMax = map.xMax
		override def zMax = map.zMax
		override def xWidth = width
		override def zWidth = width

		override def apply(x: Int, z: Int) = map(x, z)
	}
}
abstract class BWorldGenLayerTerrain[Column <: TWorldGenColumnFn: ClassTag] extends TWorldGenLayer[Column] {
	private type Region = BWorldGenLayerTerrain.Region[Column]

	protected def newGenerator(xFrom: Int, zFrom: Int, width: Int): (Int, Int) => Column

	private val cache = CacheBuilder.newBuilder().
		concurrencyLevel(Runtime.getRuntime.availableProcessors()).
		initialCapacity(100). //10x10, or 20x20 vanilla chunks.
		expireAfterWrite(15, TimeUnit.SECONDS).
		softValues().
		build(
			new CacheLoader[TupleXZ, Region] {
				override def load(key: TupleXZ) = new Region(
					BWorldGenLayerTerrain.toEdge(key.x),
					BWorldGenLayerTerrain.toEdge(key.z),
					newGenerator
				)
			}
		)

	import BWorldGenLayerTerrain.remap

	private def getAt(xRemap: Int, zRemap: Int): Region = cache.get(TupleXZ(xRemap, zRemap))

	final override def apply(x: Int, z: Int) = getAt(remap(x), remap(z)).apply(x, z)

	final override def getForChunk(where: WChunk) = getAt(remap(where.xFrom), remap(where.zFrom))

	override def getForRegion(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) = {
		val retval = new MutableMap2d[Column](xFrom, zFrom, xWidth, zWidth, null.asInstanceOf[Column])
		val xMax = xFrom+xWidth-1
		val zMax = zFrom+zWidth-1
		var xiR = remap(xFrom)
		var ziR = remap(zFrom)
		val ziRStart = ziR
		val xiRMax = remap(xMax)
		val ziRMax = remap(zMax)
		while (xiR <= xiRMax) {
			while (ziR <= ziRMax) {
				val region = getAt(xiR, ziR)
				var xiB = Math.max(region.xFrom, xFrom)
				var ziB = Math.max(region.zFrom, zFrom)
				val ziBStart = ziB
				import BWorldGenLayerTerrain.width
				val xiBMax = Math.min(region.xFrom + width - 1, xMax)
				val ziBMax = Math.min(region.zFrom + width - 1, zMax)
				while (xiB <= xiBMax) {
					while (ziB <= ziBMax) {
						retval.update(xiB, ziB, region.apply(xiB, ziB))
						ziB += 1
					}
					ziB = ziBStart
					xiB += 1
				}
				ziR += 1
			}
			ziR = ziRStart
			xiR += 1
		}
		retval
	}
}