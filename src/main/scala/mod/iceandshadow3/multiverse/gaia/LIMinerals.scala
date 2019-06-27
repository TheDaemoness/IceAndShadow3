package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.item.WUseContextBlock
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.util.E3vl

class LIMinerals extends LogicItemMulti(DomainGaia, "minerals", 1) {
	override def onUseBlock(variant: Int, context: WUseContextBlock): E3vl = {
		if(context.block.isMateria(classOf[BMateriaStoneLiving])) {
			if(context.canReplaceSide) {
				context.side.set(DomainGaia.Blocks.livingstone(0))
				context.stack.consume()
				E3vl.TRUE
			} else E3vl.FALSE
		} else E3vl.NEUTRAL
	}
}
