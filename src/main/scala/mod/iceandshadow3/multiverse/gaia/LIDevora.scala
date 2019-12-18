package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.multiverse.DomainGaia

class LIDevora(small: Boolean) extends LogicItemMulti(DomainGaia, if(small) "devora_small" else "devora", 2) {
	override def getBurnTicks(stack: WItemStack) = if(small) 200 else 1829
}