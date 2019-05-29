package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.compat.block.BlockType
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, Vec3Fixed}
import mod.iceandshadow3.util.Color

object DimensionNyx extends BDimension("nyx") {
	override def hasSkyLight = true
	override def getWorldSpawn = new Vec3Fixed(0, 64, 0)
	override def findSpawn(where: IPosChunk, check: Boolean) = null
	override def cloudLevel = 192f
	override def seaLevel = 32

	override def hasFogAt(where: IPosColumn) = true
	override def skyAngle(worldTime: Long, partialTicks: Float) = 0
	override def fogColor(skyAngle: Float, partialTicks: Float) = Color.BLACK
	override def defaultLand() = new BlockType(DomainGaia.livingstone, 0)
	override def defaultSea() = new BlockType("minecraft:air")

	override def baseDownfall = 0f
	override def baseTemperature = 0f
	override def baseAltitude = 1.0F
	override def baseHilliness = 2.5F
}
