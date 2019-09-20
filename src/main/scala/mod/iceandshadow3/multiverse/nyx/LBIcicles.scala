package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.damage.{Attack, AttackForm, Damage, TDmgTypeIce}
import mod.iceandshadow3.lib.BLogicBlockSimple
import mod.iceandshadow3.lib.block.{BlockShape, BlockSides, BlockSubCuboid}
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, WBlockRef, WBlockView}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.multiverse.DomainNyx

object LBIcicles {
	val damage = new Attack("icicles", AttackForm.CEILING, new Damage(4f) with TDmgTypeIce)
}
class LBIcicles extends BLogicBlockSimple(DomainNyx, "icicles", MatIcicle) {
	override def areSurfacesFull(variant: Int) = false

	override def harvestXP(variant: Int, what: WBlockView, silktouch: Boolean): Int = if(silktouch) 0 else 2

	override def canStayAt(variant: Int, block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Above(block).forall(BlockQueries.solid)

	override def isDiscrete = true

	override val shape: BlockShape = BlockShape(BlockSides.UNEVEN, true, BlockSubCuboid(11, 3, 14))

	override def onInside(variant: Int, block: WBlockRef, who: WEntity): Unit = {
		who.damage(LBIcicles.damage)
		block.break(true)
	}
}

