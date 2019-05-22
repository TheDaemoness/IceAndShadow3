package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.basics.common.LogicBlockOfMateria
import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.world.gaia.MatStone

object DomainGaia extends BDomain("gaia") {
	val livingstone = new LogicBlockOfMateria(this, MatStone)

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}
