package mod.iceandshadow3.basics

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.compat.block.BlockType
import mod.iceandshadow3.compat.dimension.CDimensionCoord
import mod.iceandshadow3.compat.entity.CRefEntity
import mod.iceandshadow3.compat.world.CWorld
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, IVec3}
import mod.iceandshadow3.util.Color

abstract class BDimension(val name: String) extends BBiome {
	Multiverse.addDimension(this)
	var isEnabled = false
	private var _coord: CDimensionCoord = _
	def coord = _coord
	def coord_= (where: CDimensionCoord): Unit = {_coord = where; isEnabled = true;}

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

	def placeVisitor(world: CWorld, who: CRefEntity, yaw: Float)
}
