package mod.iceandshadow3.lib.util

import mod.iceandshadow3.util.E3vl

trait TLootable {
	this: BLogic =>
	/** Used in tests to determine if a loot table should exist. */
	def shouldHaveLootTable = E3vl.TRUE.unlessTrue(this.isTechnical)
}
