package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.{BDomain, LogicItemMulti}
import mod.iceandshadow3.lib.compat.item.WRarity
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.util.Color
import mod.iceandshadow3.multiverse.nyx._

object DomainNyx extends BDomain("nyx") {
	val Blocks = new {
		val icicles = new LBIcicles
		val exousia = new LBExousia
	}
	val Items = new {
		val wayfinder = new LIWayfinder
		val icicle = new LogicItemMulti(DomainNyx, "icicle", 1)
	}
	val Sounds = new {
		val portal_subtle = WSound.addSound(DomainNyx, "portal_subtle")
		val arrival = WSound.addSound(DomainNyx, "arrival")
	}

	override def isHostileTo(other: BDomain): Boolean = other != this
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.CYAN
	override protected def baseStrength: Float = 5f
}
