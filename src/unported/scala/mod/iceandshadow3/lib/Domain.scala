package mod.iceandshadow3.lib

import mod.iceandshadow3.damage.EDeathPolicy
import mod.iceandshadow3.lib.base.ProviderLogic
import mod.iceandshadow3.lib.compat.item.WRarity
import mod.iceandshadow3.lib.util.Color
import mod.iceandshadow3.multiverse.DomainAlien

/** A collection of lore-related blocks/items/mobs/etc.
	* Every BLogic subtype eventually gets instantiated in and attached to one of these.
	* This affects their IDs as well a number of other (important) properties.
	*/
abstract class Domain(val name: java.lang.String) {
	protected[iceandshadow3] def initEarly(): Unit = {}
	protected[iceandshadow3] def initLate(): Unit = {}

	/** Color used for domain-associated text AND boss health bars.
		* Needs to be light enough to be visible against black.
		*/
	def color: Color
	def isHostileTo(other: Domain): Boolean
	def tierToRarity(tier: Int): WRarity
	def tierToDeathPolicy(tier: Int): EDeathPolicy = EDeathPolicy.DEFAULT

	/** Determines base HP and XP drops.
		*/
	protected def baseStrength: Float
	def tierToMobXP(tier: Int, isBoss: Boolean): Float = {
		val str = baseStrength
		if(isBoss) str*3+str*7*tier else str*2+(str*(1+tier))/2
	}
	def tierToMobHealthFactor(tier: Int, zone: Int): Float = baseStrength+tier+zone
	def resistsFreezing = true

	def addRecipes(): Unit = {}
	def makeName(string: String): String = s"${name}_${string}"
}

object Domain {
	def unapply(what: Object): Option[Domain] = Some(what match {
		case nicetry: Domain => nicetry
		case lp: ProviderLogic[_] => lp.getDomain
		case _ => DomainAlien
	})
}
