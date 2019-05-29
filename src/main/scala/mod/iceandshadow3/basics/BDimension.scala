package mod.iceandshadow3.basics

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.compat.block.BlockType
import mod.iceandshadow3.compat.dimension.CDimensionCoord
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, IVec3}
import mod.iceandshadow3.util.Color

abstract class BDimension(val name: String) extends BBiome {
	val late = Multiverse.addDimension(this)
	var coord: CDimensionCoord = _

	def hasSkyLight: Boolean
	def getWorldSpawn: IVec3
	def findSpawn(where: IPosChunk, check: Boolean): IVec3
	def cloudLevel: Float
	def seaLevel: Int
	def peakLevel: Int = 256
	def hasFogAt(where: IPosColumn): Boolean
	def skyAngle(worldTime: Long, partialTicks: Float): Float
	def fogColor(skyAngle: Float, partialTicks: Float): Color

	def defaultLand(): BlockType
	def defaultSea(): BlockType
}
