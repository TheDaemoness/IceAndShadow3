package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.compat.item.WRarity
import mod.iceandshadow3.lib.util.Color
import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.multiverse.gaia.ELivingstoneTypes
import mod.iceandshadow3.multiverse.polis._

object DomainPolis extends BDomain("polis") {
	val Blocks = new {
		val polished_stones = ELivingstoneTypes.values().map(new LBStone(_))
	}

	override def isHostileTo(other: BDomain): Boolean = other == DomainNyx || other == DomainGaia
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.CYAN
	override protected def baseStrength: Float = 7f
}
