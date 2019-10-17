package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain

abstract class BWorldGenLayerDecoration(override val domain: BDomain)
extends TWorldGenColumnFn with TWorldGenLayer[BWorldGenLayerDecoration] {
	override def apply(x: Int, z: Int) = this
	override def getForRegion(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) = this
}
