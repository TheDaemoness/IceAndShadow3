package mod.iceandshadow3.world.nyx

import mod.iceandshadow3.BDomain
import mod.iceandshadow3.compat.CRarity
import mod.iceandshadow3.compat.CRegistryBlock
import mod.iceandshadow3.compat.CRegistryItem

object DomainNyx extends BDomain("nyx") {
	override protected[iceandshadow3] def register(reg: CRegistryBlock): Unit = {
	}
	override protected[iceandshadow3] def register(reg: CRegistryItem): Unit = {
		reg.add(LITotemCursed)
	}
	override def isHostileTo(other: mod.iceandshadow3.BDomain): Boolean = other != this
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}