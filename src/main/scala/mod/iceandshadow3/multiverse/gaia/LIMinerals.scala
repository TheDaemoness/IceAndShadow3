package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.block.{BlockQueries, WBlockView}
import mod.iceandshadow3.lib.compat.item.WUsageItemOnBlock
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.util.{E3vl, Is}
import mod.iceandshadow3.multiverse.DomainGaia

class LIMinerals extends LogicItemMulti(DomainGaia, "minerals", 1) {

	private val canGrow = {
		import BlockQueries._
		Is.all[WBlockView](
			_.isPlain,
			Is.any(stone, sand),
			mineableByStone,
			Is.any(notHarder(2f), materia(classOf[BMateriaStoneLiving]))
		)
	}

	override def onUseBlock(variant: Int, context: WUsageItemOnBlock): E3vl = {
		val b = context.block
		if(canGrow(b)) {
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
