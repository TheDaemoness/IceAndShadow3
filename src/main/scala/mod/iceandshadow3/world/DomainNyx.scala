package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.compat.world.CSound
import mod.iceandshadow3.world.nyx.LIWayfinder

object DomainNyx extends BDomain("nyx") {
	val li_wayfinder = new LIWayfinder

	val snd_portal_subtle = CSound.addSound(this, "portal_subtle")
	val snd_arrival = CSound.addSound(this, "arrival")

	override def isHostileTo(other: BDomain): Boolean = other != this
	override def tierToRarity(tier: Int): CRarity = CRarity.COMMON
}
