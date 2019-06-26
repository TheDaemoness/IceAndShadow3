package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.subtype.LogicBlockOfMateria
import mod.iceandshadow3.lib.compat.item.WRarity
import mod.iceandshadow3.util.{Color, E3vl}
import mod.iceandshadow3.multiverse.gaia._

object DomainGaia extends BDomain("gaia") {
	val Blocks = new {
		val livingstone = new LogicBlockOfMateria(DomainGaia, MatStone)
		val navistra_bedrock = new LogicBlockOfMateria(DomainGaia, MatNavistraBedrock) {
			override def shouldHaveLootTable = E3vl.FALSE
		}
		val navistra_stone = new LogicBlockOfMateria(DomainGaia, MatNavistraStone)
	}

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.GREEN
	override protected def baseStrength: Float = 6f
}
