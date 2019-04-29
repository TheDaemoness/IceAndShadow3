package mod.iceandshadow3

import mod.iceandshadow3.compat.CRegistryBlock
import mod.iceandshadow3.compat.CRegistryItem

abstract class BDomain {
	Init.addDomain(this)
	def register(reg: CRegistryBlock): Unit
	def register(reg: CRegistryItem): Unit
}
