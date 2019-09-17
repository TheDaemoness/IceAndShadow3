package mod.iceandshadow3.lib.subtype

import mod.iceandshadow3.lib.{BDomain, BLogicItem}

class LogicItemTwoForm(domain: BDomain, name: String, tier: Int, altform: String, stacklimit: Int, stacklimitAlt: Int)
extends BLogicItem(domain, name) {
	override def countVariants = 2
	override protected def getVariantName(variant: Int) = if(variant == 1) altform else null

	override def stackLimit(variant: Int) = if(variant == 1) stacklimitAlt else stacklimit
	override def getTier(variant: Int) = tier
	def this(domain: BDomain, name: String, tier: Int, altform: String, stacklimit: Int) =
		this(domain, name, tier, altform, stacklimit, stacklimit)
	def this(domain: BDomain, name: String, tier: Int, altform: String) =
		this(domain, name, tier, altform, 64, 64)
}

