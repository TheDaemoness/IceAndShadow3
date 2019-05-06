package mod.iceandshadow3.world.gaia

import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.world.BDomain
import mod.iceandshadow3.world.nyx.DomainNyx

object DomainGaia extends BDomain("gaia") {
	override protected[iceandshadow3] def initEarly = {
		add(MatStone);
	}
	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}