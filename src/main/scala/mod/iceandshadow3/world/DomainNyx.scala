package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.world.nyx.LIWayfinder

object DomainNyx extends BDomain("nyx") {
	val wayfinder = new LIWayfinder

	override def isHostileTo(other: BDomain): Boolean = other != this
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}
