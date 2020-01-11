package mod.iceandshadow3.lib.gen

import com.google.common.cache.{CacheBuilder, CacheLoader}
import mod.iceandshadow3.lib.spatial.{IMap2d, IPosColumn, IRegion2d, TupleXZ}
import mod.iceandshadow3.lib.util.collect.FixedMap2d

import scala.reflect.ClassTag

abstract class WorldGenLayerFeatures[Column <: TWorldGenColumnFn: ClassTag, ParentColumn <: TWorldGenColumnFn](
	parent: TWorldGenLayer[ParentColumn],
	structType: WorldGenFeatureType[Column, ParentColumn]
) extends TWorldGenLayer[Column] {

	private val cache = CacheBuilder.newBuilder().
		concurrencyLevel(Runtime.getRuntime.availableProcessors()).
		weakValues().
		build(
			new CacheLoader[TupleXZ, IMap2d[Column] with IRegion2d] {
				override def load(key: TupleXZ) = {
					val origin = structureOrigin(key)
					val map = parent.getForRegion(origin.xBlock, origin.zBlock, structType.xWidth, structType.zWidth)
					structType.create(map, origin)
				}
			}
		)

	protected def structureOrigin(structureCoord: TupleXZ): IPosColumn
	protected final def getStructure(structureCoord: TupleXZ): IMap2d[Column] with IRegion2d = cache.get(structureCoord)
	protected def defaultColumn(xBlock: Int, zBlock: Int): Column

	override def getForRegion(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int): IMap2d[Column] with IRegion2d =
		new FixedMap2d[Column](
			xFrom, zFrom,
			xWidth, zWidth,
			apply
		)
}
