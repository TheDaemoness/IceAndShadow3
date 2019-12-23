package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.compat.item.WRarity
import mod.iceandshadow3.lib.common.LogicBlockOfMateria
import mod.iceandshadow3.lib.util.{Color, E3vl}
import mod.iceandshadow3.lib.{BDomain, BLogicItemSimple, LogicItemMulti}
import mod.iceandshadow3.multiverse.gaia._

object DomainGaia extends BDomain("gaia") {
	val Blocks = new {
		val livingstones = ELivingstoneTypes.values().map(new LBStoneLiving(_))
		val navistra_bedrock = new LogicBlockOfMateria(DomainGaia, Materias.navistra_bedrock) {
			override def shouldHaveLootTable = E3vl.FALSE
		}
		val navistra_stone = new LogicBlockOfMateria(DomainGaia, Materias.navistra_stone)
		val petrified_log = new LBLog("petrified_log", Materias.petrified_wood, 3)
		val petrified_leaves = new LBLeaves("petrified_leaves", Materias.petrified_leaves, petrified_log)
		val moonstone_block = new LogicBlockOfMateria(DomainGaia, Materias.moonstone) {
			override protected val baseName = "moonstone_block"
		}
	}
	val Items = new {
		val minerals = new LIMinerals
		val navistra = new LINavistra
		val cortra = new LICortra(false)
		val cortra_dust = new LICortra(true)
		val devora = new LIDevora(false)
		val devora_small = new LIDevora(true)
		val shales = ELivingstoneTypes.values().map(new LIShale(_))
		val jades = LIJade.variants.map(new LIJade(_))
		val moonstone = new LogicItemMulti(DomainGaia, "moonstone", 2)
	}

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.GREEN
	override protected def baseStrength: Float = 6f

	Recipes
}
