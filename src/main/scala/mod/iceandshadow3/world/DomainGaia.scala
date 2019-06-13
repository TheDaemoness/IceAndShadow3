package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.basics.common.LogicBlockOfMateria
import mod.iceandshadow3.compat.item.WRarity
import mod.iceandshadow3.util.Color
import mod.iceandshadow3.world.gaia._

object DomainGaia extends BDomain("gaia") {
	val Blocks = new {
		val livingstone = new LogicBlockOfMateria(DomainGaia, MatStone)
		val navistra_bedrock = new LogicBlockOfMateria(DomainGaia, MatNavistraBedrock)
		val navistra_stone = new LogicBlockOfMateria(DomainGaia, MatNavistraStone)
	}

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.GREEN
	override protected def baseStrength: Float = 6f
}
