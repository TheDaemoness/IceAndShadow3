package mod.iceandshadow3.world

import mod.iceandshadow3.compat.item.CRarity

/** Default domain for anything non-IaS.
 *  Does nothing on initialization, assumed to be hostile by other domains.
 */
object DomainAlien extends BDomain("") {
	override protected[iceandshadow3] def initEarly = {}
	override def isHostileTo(other: mod.iceandshadow3.world.BDomain): Boolean = true
	override def tierToRarity(tier: Int): CRarity = CRarity.UNCOMMON
}