package mod.iceandshadow3

import mod.iceandshadow3.compat.block.CRegistryBlock
import mod.iceandshadow3.compat.item.CRegistryItem
import mod.iceandshadow3.basics.EDeathPolicy
import mod.iceandshadow3.compat.CRarity

/** A collection of lore-related blocks/items/mobs/etc.
 */
abstract class BDomain(val name: java.lang.String) {
	Domains.addDomain(this)
	protected[iceandshadow3] def initEarly(): Unit = {}
	protected[iceandshadow3] def initLate(): Unit = {}
	protected[iceandshadow3] def register(reg: CRegistryBlock): Unit
	protected[iceandshadow3] def register(reg: CRegistryItem): Unit
	def isHostileTo(other: BDomain): Boolean
	def tierToRarity(tier: Int): CRarity
	def tierToDeathPolicy(tier: Int): EDeathPolicy = EDeathPolicy.DEFAULT
}
