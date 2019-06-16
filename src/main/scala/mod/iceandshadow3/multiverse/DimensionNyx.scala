package mod.iceandshadow3.multiverse

import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.basics.item.IItemStorage
import mod.iceandshadow3.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.compat.entity.{WEntity, WEntityLiving, WEntityPlayer}
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.compat.world.{TWWorld, WWorld}
import mod.iceandshadow3.damage.{Attack, AttackForm, BDamage, TDmgTypeCold, TDmgTypeShadow}
import mod.iceandshadow3.spatial.{IPosBlock, IPosChunk, IPosColumn, IVec3, UnitVec3s, Vec3Fixed}
import mod.iceandshadow3.util.{Color, MathUtils}
import mod.iceandshadow3.multiverse.dim_nyx.{LIFrozen, WorldSourceNyx}
import mod.iceandshadow3.multiverse.misc.Statuses

object DimensionNyx extends BDimension("nyx") {
	override def getSkyBrightness(partialTicks: Float) = 1f/15

	override def getRespawnDim = coord
	override def getWorldSpawn(world: TWWorld) = new IPosBlock {
		override def yBlock = world.height(this)+1
		override def xBlock = 0
		override def zBlock = 0
	}
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
			case player: WEntityPlayer =>
				player.setSpawnPoint(teleloc)
				freezeItems(player.inventory(), player)
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

	val placesHighAttack = new Attack("windchill", AttackForm.VOLUME, new BDamage with TDmgTypeCold {
		override def baseDamage = 1f

		override def onDamage(dmg: Float, dmgResisted: Float, what: WItemStack) = dmgResisted
	})
	val placesDarkAttack = new Attack("darkness", AttackForm.CURSE, new BDamage with TDmgTypeShadow {
		override def baseDamage = 4f

		override def onDamage(dmg: Float, dmgResisted: Float, what: WItemStack) = dmgResisted
	})

	override def onEntityLivingUpdate(who: WEntityLiving): Unit = {
		if (who.getShadowPresence >= 1f) {
			who.setStatus(Statuses.blind, 55)
			who.damage(placesDarkAttack)
		}
		val height = who.posFine.yBlock
		if (height >= 192) who.damageWithStatus(
			placesHighAttack,
			4f - MathUtils.attenuateThrough(192, height, 255) * 3f,
			Statuses.frost, 115
		)
	}
}
