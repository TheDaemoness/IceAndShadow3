package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.common.LogicItemTwoForm
import mod.iceandshadow3.multiverse.DomainGaia

class LIDevora extends LogicItemTwoForm(DomainGaia, "devora", 2, "small") {
	override def getBurnTicks(variant: Int, stack: WItemStack) = if(variant == 0) 1829 else 200
}