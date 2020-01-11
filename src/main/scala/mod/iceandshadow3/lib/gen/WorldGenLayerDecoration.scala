package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.Domain

abstract class WorldGenLayerDecoration(override val domain: Domain)
extends TWorldGenColumnFn with TWorldGenLayer[WorldGenLayerDecoration] {
	override def apply(x: Int, z: Int) = this
	override def getForRegion(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) = this
}
