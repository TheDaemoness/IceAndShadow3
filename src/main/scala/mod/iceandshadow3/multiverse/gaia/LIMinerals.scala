package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.WIdTagBlock
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
			Is.any(mineableByWood, materia(Materias.livingstone))
		)
	}

	val tagResists = WIdTagBlock("iceandshadow3:resists_minerals")

	def canGrow(what: WBlockState): Boolean = {
		!what.isComplex && !tagResists.unapply(what) && defaultBehavior(what)
	}
}
class LIMinerals extends LogicItemMulti(DomainGaia, "minerals") {
	override def onUseBlock(context: WUsageItemOnBlock) = {
		val b = context.block
		if(LIMinerals.canGrow(b)) {
			val sb = context.side
			if(BlockQueries.notHarder(b.hardness)(sb)) {
				if(sb.break(b.hardness, BlockQueries.mineableByHand(sb)) && sb.place(b.typeForPlace(context))) {
					context.stack.playSound(WSound("minecraft:block.gravel.hit"), 0.3f, 1.1f)
					b.playSound(b.soundDig) //Behavior intentional, not a bug.
					context.stack.degrade()
				}
				E3vl.TRUE
			} else E3vl.FALSE
		} else E3vl.NEUTRAL
	}
}
