package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.item.CRarity

/** Default domain for anything non-IaS.
 *  Does nothing on initialization, assumed to be hostile by other domains.
 */
object DomainAlien extends BDomain("") {
	override def isHostileTo(other: BDomain): Boolean = true
	override def tierToRarity(tier: Int): CRarity = CRarity.UNCOMMON
}