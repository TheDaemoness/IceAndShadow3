package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain

abstract class BWorldGenLayerDecoration(domain: BDomain)
extends BWorldGenColumnFn(domain) with TWorldGenLayer[BWorldGenLayerDecoration] {
	override def apply(x: Int, z: Int) = this
}
