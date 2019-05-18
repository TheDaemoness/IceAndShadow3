package mod.iceandshadow3.world.gaia

import mod.iceandshadow3.basics.LogicBlockSimple
import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.world.nyx.DomainNyx

object DomainGaia extends BDomain("gaia") {
	val livingstone = new LogicBlockSimple(this, MatStone)
	
	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}