package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.spatial.BWorldRegion

abstract class BWorldGenLayerDecoration
extends BWorldGenRegion(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
with TWorldGenLayer[BWorldGenLayerDecoration] {
	private val thisWrapped = List(this)
	override protected def remapCoord(blockCoord: Int) = 0
	override protected def getAt(xRemapped: Int, zRemapped: Int) = thisWrapped
	override def getForChunk(where: WChunk) = thisWrapped
	override def getForRegion(where: BWorldRegion) = thisWrapped
}
