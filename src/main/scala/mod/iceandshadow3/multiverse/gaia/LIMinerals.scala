package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.lib.compat.item.WUseContextBlock
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainGaia

class LIMinerals extends LogicItemMulti(DomainGaia, "minerals", 1) {
	override def onUseBlock(variant: Int, context: WUseContextBlock): E3vl = {
		import BlockQueries._
		val b = context.block
		if(
			b.isPlain &&
				b.isAny(stone, sand) &&
				b.isAll(mineableByStone) &&
				b.isAny(notHarder(2f), materia(classOf[BMateriaStoneLiving]))
		) {
			if(context.canReplaceSide) {
				if(context.side.place(context.block.typeDefault)) {
					context.user.playSound(WSound("minecraft:block.gravel.hit"), 0.3f, 1.1f)
					context.block.playSound(context.block.soundDig) //Behavior intentional, not a bug.
					context.stack.consume()
				}
				E3vl.TRUE
			} else E3vl.FALSE
		} else E3vl.NEUTRAL
	}
}
