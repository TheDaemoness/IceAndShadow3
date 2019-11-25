package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.{Cells, IPosColumn, TupleXZ, RandomXZ}

import scala.reflect.ClassTag

/** Layer that generates structures that are positioned within a grid.
	* Said structures will not overlap or cross grid line boundries.
	*/
abstract class BWorldGenLayerStructuresSparse[Column <: TWorldGenColumnFn: ClassTag, ParentColumn <: TWorldGenColumnFn](
	seed: Long, mod: Int, parent: TWorldGenLayer[ParentColumn], structType: BWorldGenStructureType[Column, ParentColumn],
	variance: Int, margin: Int
) extends BWorldGenLayerStructures[Column, ParentColumn](parent, structType) {
	private val xWidthRegion = structType.xWidth+variance+margin*2
	private val zWidthRegion = structType.zWidth+variance+margin*2
	private final def remap(xBlock: Int, zBlock: Int): TupleXZ =
		TupleXZ(Cells.rescale(xBlock, xWidthRegion), Cells.rescale(zBlock, zWidthRegion))

	override def apply(x: Int, z: Int) = {
		val structInstance = getStructure(remap(x, z))
		if(structInstance.isInside(x, z)) structInstance.apply(x, z)
		else defaultColumn(x, z)
	}

	override protected def structureOrigin(structureCoord: TupleXZ) = {
		val rng = new RandomXZ(seed, mod, structureCoord.x, structureCoord.z)
		val xFrom = margin+Cells.cellEdge(xWidthRegion, structureCoord.x)
		val zFrom = margin+Cells.cellEdge(zWidthRegion, structureCoord.z)
		IPosColumn.wrap(xFrom+rng.nextInt(1+variance), zFrom+rng.nextInt(1+variance))
	}
}
