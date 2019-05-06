package mod.iceandshadow3.world.nyx
import mod.iceandshadow3.world.nyx._
import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.world.BDomain

object DomainNyx extends BDomain("nyx") {
	override protected[iceandshadow3] def initEarly = {
		add(LITotemCursed);
	}
	override def isHostileTo(other: BDomain): Boolean = other != this
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}