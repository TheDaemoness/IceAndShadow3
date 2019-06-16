package mod.iceandshadow3.multiverse

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.item.WRarity
import mod.iceandshadow3.util.Color
import mod.iceandshadow3.multiverse.dim_nyx.LIFrozen

/** Default domain for anything non-IaS. A catch-all for everything with no other sensible domain.
 */
object DomainAlien extends BDomain("alien") {
	override def isHostileTo(other: BDomain): Boolean = true
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.BLUE
	override protected def baseStrength: Float = 5f
	override def resistsFreezing = false

	val frozen = new LIFrozen
}