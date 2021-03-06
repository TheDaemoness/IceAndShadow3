package mod.iceandshadow3.multiverse

import mod.iceandshadow3.damage._
import mod.iceandshadow3.lib.LogicDimension
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityLiving, WEntityPlayer}
import mod.iceandshadow3.lib.compat.world.{TWWorld, WDimensionCoord, WWorld}
import mod.iceandshadow3.lib.item.ItemSeq
import mod.iceandshadow3.lib.spatial.{IPosBlock, UnitVec3s}
import mod.iceandshadow3.lib.util.{Color, MathUtils}
import mod.iceandshadow3.lib.world.{BHandlerFog, BHandlerSky}
import mod.iceandshadow3.multiverse.dim_nyx.{LIFrozen, WorldGenNyx}
import mod.iceandshadow3.multiverse.misc.StatusEffects

object DimensionNyx extends LogicDimension("nyx") {
	override def getRespawnDim = coord
	override def getWorldSpawn(world: TWWorld) = new IPosBlock {
		override def yBlock = 1 + world.heightAt(this)
		override def xBlock = 0
		override def zBlock = 0
	}
	override def isSurface = true
	override def cloudLevel = 192f
	override def seaLevel = 8


	override val handlerFog = BHandlerFog.black
	override val handlerSky = new BHandlerSky {
		override def hasLuma = true
		override def isDay(world: WWorld) = false
		//override def luma(world: WWorld, partialTicks: Float) = 1f/15
		override def angle(world: WWorld, worldTime: Long, partialTicks: Float) = 0.5f
		//override def colorDefault = Color(0x000408)
		//override def stars(world: WWorld, partialTicks: Float) = 0.5f
	}

	override def baseDownfall = 0f
	override def baseTemperature = 0f

	override def defaultPlace(where: WWorld) = {
		val topopt = where.topSolid(UnitVec3s.ZERO)
		if(topopt.isEmpty) UnitVec3s.ZERO else topopt.get.asMutable.add(0.0, 1.4, 0.0)
	}

	override def onArrivalPost(player: WEntityPlayer): Unit = {
		val where = defaultPlace(player.world())
		player.teleport(where)
		player.setSpawnPoint(where)
		freezeItems(player.inventory(), player)
	}

	def isValidEscapePoint(where: IPosBlock) = {
		Math.abs(where.xBlock) <= 3 && Math.abs(where.zBlock) <= 3
	}

	override def onDeparture(who: WEntity, where: WDimensionCoord) = {
		if(isValidEscapePoint(who.posCoarse)) true
		else {
			who.kill()
			false
		}
	}

	override def getWorldGen(seed: Long) = new WorldGenNyx(seed)

	override def brightnessTable(lightBrightnessTable: Array[Float]): Unit = {
		for(i <- lightBrightnessTable.indices) {
			val light = i / 16.0F
			lightBrightnessTable(i) = (light*light)/(1+light)
		}
	}

	override def modifyLightmap(lightSky: Float, lightBlock: Float, colorIn: Color): Color = {
		if(lightSky > 0.001f) colorIn.multiply(0.75f)
		else {
			val multiplier = Math.cbrt(lightBlock).toFloat
			colorIn.multiply(multiplier)
		}
	}

	def freezeItems(container: ItemSeq, player: WEntityPlayer): Unit = {
		//Dimension check should be handled elsewhere!
		if(player.isCreative) return
		var frozeAnything = false
		container.mapInPlace(original => {
			val result = LIFrozen.freeze(original, player.world(), Some(player))
			frozeAnything |= result.isDefined
			result.getOrElse(original)
		})
	}

	val placesHighAttack = Attack(
		"windchill", AttackForm.VOLUME,
		new DamageWithStatus(1f, StatusEffects.frost.forTicks(119)) with TDmgTypeCold
	)
	val placesDarkAttack = Attack(
		"darkness", AttackForm.CURSE,
		new DamageWithStatus(4f, StatusEffects.blind.forTicks(65)) with TDmgTypeShadow
	)

	override def onEntityLivingUpdate(who: WEntityLiving): Unit = {
		if (who.getShadowPresence >= 1f) placesDarkAttack(who)
		val height = who.posFine.yBlock
		if (height >= 192) placesHighAttack.*(4f - MathUtils.ratioBelow(192, height, 255) * 3f)(who)
	}
}
