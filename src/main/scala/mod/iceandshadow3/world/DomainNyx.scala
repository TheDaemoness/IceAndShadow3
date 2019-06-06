package mod.iceandshadow3.world

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.compat.item.WRarity
import mod.iceandshadow3.compat.world.WSound
import mod.iceandshadow3.world.nyx._

object DomainNyx extends BDomain("nyx") {
	val li_wayfinder = new LIWayfinder

	val snd_portal_subtle = WSound.addSound(this, "portal_subtle")
	val snd_arrival = WSound.addSound(this, "arrival")

	override def isHostileTo(other: BDomain): Boolean = other != this
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON
}
