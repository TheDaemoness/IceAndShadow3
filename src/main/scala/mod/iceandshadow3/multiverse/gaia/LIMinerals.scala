package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.block.{BlockQueries, WBlockState}
import mod.iceandshadow3.lib.compat.item.WUsageItemOnBlock
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.util.{E3vl, Is}
import mod.iceandshadow3.multiverse.DomainGaia

object LIMinerals {
	private val defaultBehavior = {
		import BlockQueries._
		Is.all[WBlockState](
			Is.any(stone, sand, glass),
			Is.any(mineableByWood, materia(classOf[TMateriaGrowable]))
		)
	}

	def canGrow(what: WBlockState): Boolean = {
		!what.isComplex && !what.hasTag("iceandshadow3:resists_minerals") && defaultBehavior(what)
	}
}
class LIMinerals extends LogicItemMulti(DomainGaia, "minerals", 1) {
	override def onUseBlock(variant: Int, context: WUsageItemOnBlock): E3vl = {
		val b = context.block
		if(LIMinerals.canGrow(b)) {
			val sb = context.side
			if(BlockQueries.notHarder(b.hardness)(sb)) {
				if(sb.break(b.hardness, BlockQueries.mineableByHand(sb)) && sb.place(b.typeForPlace(context))) {
					context.user.playSound(WSound("minecraft:block.gravel.hit"), 0.3f, 1.1f)
					b.playSound(b.soundDig) //Behavior intentional, not a bug.
					context.stack.consume()
				}
				E3vl.TRUE
			} else E3vl.FALSE
		} else E3vl.NEUTRAL
	}
}
