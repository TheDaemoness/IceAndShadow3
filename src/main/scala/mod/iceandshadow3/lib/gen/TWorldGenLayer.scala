package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.world.WChunk
import mod.iceandshadow3.lib.spatial.BWorldRegion

import scala.collection.immutable

trait TWorldGenLayer[+Region <: BWorldGenRegion] {
	protected def remapCoord(blockCoord: Int): Int
	protected def getAt(xRemapped: Int, zRemapped: Int): Seq[Region]
	def getForRegion(where: BWorldRegion): Seq[Region] = {
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
		builder.result()
	}
	def getForChunk(where: WChunk): Seq[Region] = getForRegion(where)
}
