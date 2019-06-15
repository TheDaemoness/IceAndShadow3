package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.basics.item.IItemStorage
import mod.iceandshadow3.compat.ResourceMap
import mod.iceandshadow3.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.compat.entity.{CNVEntity, WEntity, WEntityPlayer}
import mod.iceandshadow3.compat.world.WWorld
import mod.iceandshadow3.spatial.{IPosChunk, IPosColumn, IVec3, UnitVec3s, Vec3Fixed}
import mod.iceandshadow3.util.{Color, StringMap}
import mod.iceandshadow3.world.dim_nyx.{LIFrozen, WorldSourceNyx}

object DimensionNyx extends BDimension("nyx") {
	override def getSkyBrightness(partialTicks: Float) = 1f/15
	override def getWorldSpawn = new Vec3Fixed(0, 64, 0)
	override def findSpawn(where: IPosChunk, check: Boolean) = null
	override def cloudLevel = 192f
	override def seaLevel = 8

	override def hasFogAt(where: IPosColumn) = true
	override def skyAngle(worldTime: Long, partialTicks: Float) = 0
	override def fogColor(skyAngle: Float, partialTicks: Float) = Color.BLACK
	override def defaultLand() = new BlockTypeSimple(DomainGaia.Blocks.livingstone, 0)
	override def defaultSea() = new BlockTypeSimple("minecraft:air")

	override def baseDownfall = 0f
	override def baseTemperature = 0f

	override def handleArrival(here: WWorld, who: WEntity): IVec3 = {
		//TODO: Change once the central fort is back in place.
		val topopt = here.topSolid(UnitVec3s.ZERO)
		val teleloc = if(topopt.isEmpty) UnitVec3s.ZERO else topopt.get.asMutable.add(0.0, 1.4, 0.0)
		who match {
			case player: WEntityPlayer => freezeItems(player.inventory(), player)
			case _ => //Definitely come up with something for other entities too.
		}
		teleloc
	}

	override def getWorldSource(seed: Long) = new WorldSourceNyx(seed)

	override def brightnessTable(lightBrightnessTable: Array[Float]): Unit = {
		for(i <- lightBrightnessTable.indices) {
			val light = i / 15.0F
			lightBrightnessTable(i) = (light*light)/(1+light)
		}
	}

	override def modifyLightmap(lightSky: Float, lightBlock: Float, colorIn: Color): Color = {
		if(lightSky > 0.001f) colorIn
		else {
			val lightness = lightSky/0.001f
			val multiplier = lightness+(1-lightness)*Math.cbrt(lightBlock).toFloat
			colorIn.multiply(multiplier)
		}
	}

	def freezeItems(container: IItemStorage, player: WEntityPlayer): Unit = {
		//Dimension check should be handled elsewhere!
		if(player.isCreative) return
		var frozeAnything = false
		container.transform(original => {
			val result = LIFrozen.freeze(original, Some(player))
			frozeAnything |= result.isDefined
			result.getOrElse(original)
		})
	}
}
