package mod.iceandshadow3.basics

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.compat.world.CDimensionCoord
import mod.iceandshadow3.spatial.IVec3

abstract class BDimension(val name: String) {
	val late = Multiverse.addDimension(this)
	var coord: CDimensionCoord = _

	def hasSkyLight: Boolean
	def getWorldSpawn: IVec3
	def findSpawn(xChunk: Int, zChunk: Int, check: Boolean): IVec3
	def cloudHeight: Float
	def hasFogAt(xBlock: Int, zBlock: Int): Boolean
	def skyAngle(worldTime: Long, partialTicks: Float): Float
}
