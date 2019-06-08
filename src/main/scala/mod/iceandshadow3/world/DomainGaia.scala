package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.basics.common.LogicBlockOfMateria
import mod.iceandshadow3.compat.item.WRarity
import mod.iceandshadow3.world.gaia._

object DomainGaia extends BDomain("gaia") {
	val lb_livingstone = new LogicBlockOfMateria(this, MatStone)
	val lb_navistra_bedrock = new LogicBlockOfMateria(this, MatNavistraBedrock)
	val lb_navistra_stone = new LogicBlockOfMateria(this, MatNavistraStone)

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON
}
