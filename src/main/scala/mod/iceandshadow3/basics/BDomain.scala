package mod.iceandshadow3.basics

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.compat.item.WRarity
import mod.iceandshadow3.damage.EDeathPolicy

/** A collection of lore-related blocks/items/mobs/etc.
	* Every BLogic subtype eventually gets instantiated in and attached to one of these.
	* This affects their IDs as well a number of other (important) properties.
	*/
abstract class BDomain(val name: java.lang.String) {
	Multiverse.addDomain(this)
	
	protected[iceandshadow3] def initEarly(): Unit = {}
	protected[iceandshadow3] def initLate(): Unit = {}
	def isHostileTo(other: BDomain): Boolean
	def tierToRarity(tier: Int): WRarity
	def tierToDeathPolicy(tier: Int): EDeathPolicy = EDeathPolicy.DEFAULT
}
