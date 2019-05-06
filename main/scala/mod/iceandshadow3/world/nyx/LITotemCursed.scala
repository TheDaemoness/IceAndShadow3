package mod.iceandshadow3.world.nyx

// NOTE: As the first item added to IceAndShadow3, this one has the special distinction of being on IaS3's creative tab.
// See SCreative tab in the compat package.

import mod.iceandshadow3.basics.BLogicItem

object LITotemCursed extends BLogicItem(DomainNyx, "totem_cursed") {
	override def stackLimit(variant: Int) = 1
}