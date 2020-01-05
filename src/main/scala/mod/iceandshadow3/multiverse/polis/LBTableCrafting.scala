package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.file.BJsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.compat.item.container.WContainerSource
import mod.iceandshadow3.multiverse.DomainPolis

class LBTableCrafting extends LogicBlock(DomainPolis, "table_crafting", Materias.petrified_brick) {
	override def container(us: WBlockRef) = WContainerSource.crafting(us)

	override def onUsed(us: WBlockRef, item: WItemStackOwned[WEntityPlayer]) = {
		us.container().openAs(item.owner)
		true
	}

	override def getGenAssetsBlock = Some(BJsonGenAssetsBlock.customSingleModel(this))
}
