package mod.iceandshadow3.world.nyx
import mod.iceandshadow3.world.nyx._
import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.basics.BDomain

object DomainNyx extends BDomain("nyx") {
	val wayfinder = new LIWayfinder
	
	override def isHostileTo(other: BDomain): Boolean = other != this
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}