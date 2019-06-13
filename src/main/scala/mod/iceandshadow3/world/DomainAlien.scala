package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.item.WRarity
import mod.iceandshadow3.util.Color

/** Default domain for anything non-IaS.
 *  Should NOT be used by anything IaS. Assumed to be hostile by other domains.
 */
object DomainAlien extends BDomain(null) {
	override def isHostileTo(other: BDomain): Boolean = true
	override def tierToRarity(tier: Int): WRarity = WRarity.UNCOMMON

	override def color = Color.BLUE
	override protected def baseStrength: Float = 5f
}