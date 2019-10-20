package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.damage.{Attack, AttackForm, Damage, TDmgTypeIce}
import mod.iceandshadow3.lib.LogicBlockSimple
import mod.iceandshadow3.lib.block.{BlockShape, BlockSubCuboid}
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, Materia, WBlockRef, WBlockView}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.multiverse.DomainNyx

object LBIcicles {
	val materia =
		Materia.builder(Materia.ice_blue).hardness(0f).luma(1)("icicle")
	val damage = Attack("icicles", AttackForm.CEILING, new Damage(4f) with TDmgTypeIce)
}
class LBIcicles extends LogicBlockSimple(DomainNyx, "icicles", LBIcicles.materia) {
	override def areSurfacesFull(variant: Int) = false

	override def harvestXP(variant: Int, what: WBlockView, silktouch: Boolean): Int = if(silktouch) 0 else 2

	override def canStayAt(variant: Int, block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Above(block).forall(BlockQueries.solid)

	override def isDiscrete = true

	override val shape: BlockShape = BlockShape(true, BlockSubCuboid(11, 3, 14))

	override def onInside(variant: Int, block: WBlockRef, who: WEntity): Unit = {
		LBIcicles.damage(who)
		block.break(true)
	}

	override def getBlockModelGen(variant: Int) = Some(BJsonAssetGen.blockDeco)
}

