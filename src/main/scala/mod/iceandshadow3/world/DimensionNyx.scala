package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.compat.block.{BBlockType, BlockTypeSimple}
import mod.iceandshadow3.compat.entity.CRefEntity
import mod.iceandshadow3.compat.world.CWorld
import mod.iceandshadow3.gen.{BWorldSource, Cellmaker, Noise2dCrater}
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, SpatialConstants, Vec3Fixed}
import mod.iceandshadow3.util.{Color, SMath}

class WorldSourceNyx(seed: Long) extends BWorldSource {
	val stonetype = new BlockTypeSimple(DomainGaia.livingstone, 0)
	val plateaumaker = new Cellmaker(seed, 9967, 270)
	val noisemakerDip = new Noise2dCrater(seed, 1928, 60)
	val noisemakerMountain = new Noise2dCrater(seed, 3092, 150)
	val noisemakerRidgeScale = new Noise2dCrater(seed, 4815, 420)
	val noisemakerRidgeB = new Noise2dCrater(seed, 6872, 230)
	val noisemakerIsle = new Noise2dCrater(seed, 4815, 1200)
	override def getColumn(x: Int, z: Int): Iterator[BBlockType] = {
		val ridgescale = Math.sqrt(1-noisemakerRidgeScale(x,z))
		var cratervalue = noisemakerDip(x,z)
		cratervalue *= Math.cbrt(cratervalue)/(4-ridgescale)
		val mountainvalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerMountain(x,z)*Math.PI)))/2
		val ridgevalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerRidgeB(x,z)*Math.PI)))/2
		val islevalue = 1-noisemakerIsle(x,z)
		val height = {
			if(islevalue <= 0.15) 0
			else {
				val tuner = if(islevalue <= 0.3) (islevalue-0.2)*10 else 1d
				val base = 2+(islevalue+mountainvalue*tuner)*(1+SMath.sinelike(islevalue))+ridgevalue*tuner+cratervalue
				if(islevalue <= 0.2) base*(islevalue-0.15)*20
				else base
			}
		}
		new Iterator[BBlockType]() {
			var y = 0
			val finalheight = height
			override def hasNext = y/32f < finalheight

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
