package mod.iceandshadow3.lib.gen

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.spatial.{IPosColumn, PairXZ}
import mod.iceandshadow3.lib.util.collect.{FixedMap2d, IMap2d, IRegion2d}

import scala.reflect.ClassTag

abstract class BWorldGenLayerStructures[Column <: BWorldGenColumnFn: ClassTag, ParentColumn <: BWorldGenColumnFn](
	parent: TWorldGenLayer[ParentColumn],
	structType: BWorldGenStructureType[Column, ParentColumn]
) extends TWorldGenLayer[Column] {

	private val cache = CacheBuilder.newBuilder().
		concurrencyLevel(Runtime.getRuntime.availableProcessors()).
		weakValues().
		build(
			new CacheLoader[PairXZ, IMap2d[Column] with IRegion2d] {
				override def load(key: PairXZ) = {
					val origin = structureOrigin(key)
					val map = parent.getForRegion(origin.xBlock, origin.zBlock, structType.xWidth, structType.zWidth)
					structType.apply(map)
				}
			}
		)

	protected def structureOrigin(structureCoord: PairXZ): IPosColumn
	protected final def getStructure(structureCoord: PairXZ): IMap2d[Column] with IRegion2d = cache.get(structureCoord)
	protected def defaultColumn(xBlock: Int, zBlock: Int): Column

	override def getForRegion(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int): IMap2d[Column] with IRegion2d =
		new FixedMap2d[Column](
			xFrom, zFrom,
			xWidth, zWidth,
			apply
		)
}
