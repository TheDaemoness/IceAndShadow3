package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.damage.{Attack, AttackForm, DamageSimple, TDmgTypeIce}
import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.block.{BlockShape, BlockSubCuboid}
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, Materia, WBlockRef, WBlockView}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.JsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.loot.{Loot, LootBuilder, WLootContextBlock}
import mod.iceandshadow3.multiverse.DomainNyx

object LBIcicles {
	val materia =
		Materia.builder(Materia.ice_blue).hardness(0f).luma(1)("icicle")
	val damage = Attack("icicles", AttackForm.CEILING, new DamageSimple(4f) with TDmgTypeIce)
}
class LBIcicles extends LogicBlock(DomainNyx, "icicles", LBIcicles.materia) {
	override def areSurfacesFull = false

	override def harvestXP(what: WBlockView, silktouch: Boolean): Int = if(silktouch) 0 else 2

	override def canStayAt(block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Above(block).forall(BlockQueries.solid)

	override def isDiscrete = true

	override val shape: BlockShape = BlockShape(true, BlockSubCuboid(11, 3, 14))

	final override val handlerEntityInside = (us: WBlockRef, who: WEntity) => {
		LBIcicles.damage(who)
		us.break(true)
	}

	override def getGenAssetsBlock = Some(JsonGenAssetsBlock.deco(this))

	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit = {
		what.addOne(Loot.silktouch(this).orElse(Loot.of(DomainNyx.Items.icicle.toWItemType, 2, 2)))
	}
}

