package mod.iceandshadow3.basics

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.compat.world.CDimensionCoord
import mod.iceandshadow3.util.Vec3

abstract class BDimension(val name: String) {
	val late = Multiverse.addDimension(this)
	var coord: CDimensionCoord = _

	def hasSkyLight: Boolean
	def getWorldSpawn: Vec3
	def findSpawn(xChunk: Int, zChunk: Int, check: Boolean): Vec3
	def cloudHeight: Float
	def hasFogAt(xBlock: Int, zBlock: Int): Boolean
	def skyAngle(worldTime: Long, partialTicks: Float): Float
}
