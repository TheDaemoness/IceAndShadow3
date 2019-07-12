package mod.iceandshadow3.gen

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.spatial.PairXZ
import mod.iceandshadow3.util.collect.{FixedMap2d, IMap2d}

import scala.reflect.ClassTag

abstract class BWorldGenData[ColumnDataType: ClassTag] {
	final val regionWidth = 32 //2x chunk width, which should be aligned under Cellmaker logic.
	protected def newRegion(xFrom: Int, zFrom: Int): IMap2d[ColumnDataType]
	private val cache = CacheBuilder.newBuilder().
		maximumSize(36).
		expireAfterAccess(1, TimeUnit.MINUTES).
		build(
			new CacheLoader[PairXZ, IMap2d[ColumnDataType]]{
				override def load(key: PairXZ) = newRegion(
					Cellmaker.rescale(key.x, regionWidth),
					Cellmaker.rescale(key.z, regionWidth)
				)
			}
		)
	final def region(xBlock: Int, zBlock: Int): IMap2d[ColumnDataType] =
		cache.get(PairXZ(Cellmaker.rescale(xBlock, regionWidth), Cellmaker.rescale(zBlock, regionWidth)))
	final def region(xzCell: PairXZ): IMap2d[ColumnDataType] = cache.get(xzCell)
	final def column(xzBlock: PairXZ): ColumnDataType = cache.get(xzBlock)(xzBlock.x, xzBlock.z)
}
