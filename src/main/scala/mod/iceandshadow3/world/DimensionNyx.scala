package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.compat.block.{BBlockType, BlockTypeSimple}
import mod.iceandshadow3.compat.entity.CRefEntity
import mod.iceandshadow3.compat.world.CWorld
import mod.iceandshadow3.gen.{BWorldSource, Noise2dCell}
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, SpatialConstants, Vec3Fixed}
import mod.iceandshadow3.util.Color

class WorldSourceNyx(seed: Long) extends BWorldSource {
	val stonetype = new BlockTypeSimple(DomainGaia.livingstone, 0)
	val noisemakerDip = new Noise2dCell(seed, 1928, 60)
	val noisemakerHill = new Noise2dCell(seed, 3092, 150)
	override def getColumn(x: Int, z: Int): Iterator[BBlockType] = {
		var cratervalue = 1-noisemakerDip(x,z)
		cratervalue *= Math.cbrt(cratervalue)
		val hillvalue = (1-Math.cos((1-noisemakerHill(x,z))*Math.PI))/2

		new Iterator[BBlockType]() {
			var y = 0
			val height = cratervalue/3+hillvalue
			override def hasNext = (y-32)/32f < height

			override def next() = {
				y += 1
				stonetype
			}
		}
	}
}

object DimensionNyx extends BDimension("nyx") {
	override def hasSkyLight = true //TODO: Dim light.
	override def getWorldSpawn = new Vec3Fixed(0, 64, 0)
	override def findSpawn(where: IPosChunk, check: Boolean) = null
	override def cloudLevel = 192f
	override def seaLevel = 32

	override def hasFogAt(where: IPosColumn) = true
	override def skyAngle(worldTime: Long, partialTicks: Float) = 0
	override def fogColor(skyAngle: Float, partialTicks: Float) = Color.BLACK
	override def defaultLand() = new BlockTypeSimple(DomainGaia.livingstone, 0)
	override def defaultSea() = new BlockTypeSimple("minecraft:air")

	override def baseDownfall = 0f
	override def baseTemperature = 0f
	override def baseAltitude = 1.0F
	override def baseHilliness = 2.5F

	override def placeVisitor(here: CWorld, who: CRefEntity, yaw: Float): Unit = {
		//TODO: Bit shoddy. Come up with something better.
		val topopt = here.topSolid(SpatialConstants.ZERO)
		val teleloc = if(topopt.isEmpty) SpatialConstants.ZERO else topopt.get.asMutable.add(0.0, 1.4, 0.0)
		//DomainNyx.snd_arrival.play(here, teleloc, 1f, 1f)
		//TODO: ^ Research if there's a way to override the vanilla teleport sound.
		who.teleport(teleloc)
	}

	override def getWorldSource(seed: Long) = new WorldSourceNyx(seed)
}
