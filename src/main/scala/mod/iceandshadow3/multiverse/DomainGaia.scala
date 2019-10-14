package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.compat.item.WRarity

import mod.iceandshadow3.lib.subtype.{LogicBlockOfMateria, LogicItemTwoForm}
import mod.iceandshadow3.lib.util.{Color, E3vl}
import mod.iceandshadow3.lib.{BDomain, LogicItemMulti}
import mod.iceandshadow3.multiverse.gaia._

object DomainGaia extends BDomain("gaia") {
	val Blocks = new {
		val livingstone = new LBStoneLiving
		val navistra_bedrock = new LogicBlockOfMateria(DomainGaia, MatNavistraBedrock) {
			override def shouldHaveLootTable = E3vl.FALSE
		}
		val navistra_stone = new LogicBlockOfMateria(DomainGaia, MatNavistraStone)
		val petrified_log = new LBLog("petrified_log", MatPetrifiedWood, 3)
		val petrified_leaves = new LBLeaves("petrified_leaves", MatPetrifiedLeaves, petrified_log)
		val petrified_bricks = new LogicBlockOfMateria(DomainGaia, MatPetrifiedWood) {
			override protected def getBaseName = "petrified_bricks"
		}
	}
	val Items = new {
		val minerals = new LIMinerals
		val navistra = new LINavistra
		val cortra = new LogicItemTwoForm(DomainGaia, "cortra", 2, "dust")
		val devora = new LIDevora
		val shale = new LIShale
		val petrified_brick = new LogicItemMulti(DomainGaia, "petrified_brick", 1)
		val jade = new LIJade
	}

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.GREEN
	override protected def baseStrength: Float = 6f

	Recipes
}
