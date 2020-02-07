package mod.iceandshadow3.multiverse

import mod.iceandshadow3.lib.compat.item.WRarity
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.util.Color
import mod.iceandshadow3.lib.{Domain, LogicItemMulti}
import mod.iceandshadow3.multiverse.nyx.{LINifelhium, _}

object DomainNyx extends Domain("nyx") {
	val Blocks = new {
		val icicles = new LBIcicles
		val exousia = new LBExousia
		val nifelhiumCrystal = new LBNifelhiumCrystal
	}
	val Items = new {
		val wayfinder = new LIWayfinder
		val icicle = new LogicItemMulti(DomainNyx, "icicle")
		val nifelhium = new LINifelhium(false)
		val nifelhium_small = new LINifelhium(true)
		val bone = new LogicItemMulti(DomainNyx, "bone")
	}
	val Sounds = new {
		val portal_subtle = WSound.addSound(DomainNyx, "portal_subtle")
		val arrival = WSound.addSound(DomainNyx, "arrival")
	}

	override def isHostileTo(other: Domain): Boolean = other != this
	override def tierToRarity(tier: Int): WRarity = WRarity.COMMON

	override def color = Color.CYAN
	override protected def baseStrength: Float = 5f
}
