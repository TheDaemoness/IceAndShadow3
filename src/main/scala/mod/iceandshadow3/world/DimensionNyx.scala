package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.compat.entity.WEntity
import mod.iceandshadow3.compat.world.WWorld
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, SUnitVec3s, Vec3Fixed}
import mod.iceandshadow3.util.Color
import mod.iceandshadow3.world.dim_nyx.WorldSourceNyx

object DimensionNyx extends BDimension("nyx") {
	override def getSkyBrightness(partialTicks: Float) = 0.05f
	override def getWorldSpawn = new Vec3Fixed(0, 64, 0)
	override def findSpawn(where: IPosChunk, check: Boolean) = null
	override def cloudLevel = 192f
	override def seaLevel = 8

	override def hasFogAt(where: IPosColumn) = true
	override def skyAngle(worldTime: Long, partialTicks: Float) = 0
	override def fogColor(skyAngle: Float, partialTicks: Float) = Color.BLACK
	override def defaultLand() = new BlockTypeSimple(DomainGaia.livingstone, 0)
	override def defaultSea() = new BlockTypeSimple("minecraft:air")

	override def baseDownfall = 0f
	override def baseTemperature = 0f

	override def placeVisitor(here: WWorld, who: WEntity, yaw: Float): Unit = {
		//TODO: Bit shoddy. Come up with something better.
		val topopt = here.topSolid(SUnitVec3s.ZERO)
		val teleloc = if(topopt.isEmpty) SUnitVec3s.ZERO else topopt.get.asMutable.add(0.0, 1.4, 0.0)
		//DomainNyx.snd_arrival.play(here, teleloc, 1f, 1f)
		//TODO: ^ Research if there's a way to override the vanilla teleport sound.
		who.teleport(teleloc)
	}

	override def getWorldSource(seed: Long) = new WorldSourceNyx(seed)

	override def brightnessTable(lightBrightnessTable: Array[Float]): Unit = {
		for(i <- lightBrightnessTable.indices) {
			val sub = 1.0F - i / 15.0F
			lightBrightnessTable(i) = (1.0F - sub) / (3*sub + 1.0F)
		}
	}


}
