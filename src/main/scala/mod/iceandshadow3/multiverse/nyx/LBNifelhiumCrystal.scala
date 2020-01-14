package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.block.{BlockShape, BlockSubCuboid}
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, Materia, WBlockRef, WBlockState, WBlockView}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.JsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.loot.{LootBuilder, WLootContextBlock}
import mod.iceandshadow3.multiverse.DomainNyx

object LBNifelhiumCrystal {
	val materia =
		Materia.builder(Materia.ice_blue).hardness(0f).opacity(8)("nifehlium_crystal")
}
class LBNifelhiumCrystal extends LogicBlock(DomainNyx, "nifelhium_crystal", LBNifelhiumCrystal.materia) {
	override def areSurfacesFull = false
	override def harvestXP(what: WBlockView, silktouch: Boolean): Int = 13
	override def canStayAt(block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Below(block).forall(BlockQueries.solid)
	override def isDiscrete = true
	override def shape = BlockShape(BlockSubCuboid(10, 0, 13)) //Slightly inside the texture bounds.
	final override val handlerEntityInside = (us: WBlockRef, _: WEntity) => {
		us.break(true)
	}
	override def onReplaced(us: WBlockState, them: WBlockRef, moved: Boolean): Unit = {
		//TODO: AoE damage burst that creates icicles.
	}

	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit =
		what.addOne(DomainNyx.Items.nifelhium.toWItemStack)

	override def getGenAssetsBlock = Some(JsonGenAssetsBlock.deco(this))
}
