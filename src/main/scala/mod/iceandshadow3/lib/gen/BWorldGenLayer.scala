package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.spatial.BWorldRegion

import scala.collection.immutable

abstract class BWorldGenLayer[+Region <: BWorldGenRegion] {
	protected def remapCoord(blockCoord: Int): Int
	protected def getAt(xRemapped: Int, zRemapped: Int): Iterator[Region]
	def getForRegion(where: BWorldRegion): Iterator[Region] = {
		var x = remapCoord(where.xFrom)
		var z = remapCoord(where.zFrom)
		val xLast = remapCoord(where.xMax)
		val zLast = remapCoord(where.zMax)
		val builder = immutable.List.newBuilder[Region]
		while(x <= xLast) {
			while(z <= zLast) {
				getAt(x, z).foreach(builder.addOne)
				z += 1
			}
			x += 1
		}
		builder.result().iterator
	}
	def getForChunk(where: WChunk): Iterator[Region] = getForRegion(where)
}
