package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.compat.block.{Materia, WBlockRef}
import mod.iceandshadow3.lib.compat.inventory.WContainerSource
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.file.JsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainPolis

class LBTableCrafting(name: String, mat: Materia) extends LogicBlock(DomainPolis, name, mat) {
	override def container(us: WBlockRef) = WContainerSource.crafting(us)

	override def onUsed(us: WBlockRef, item: WItemStackOwned[WEntityPlayer]) = {
		us.container().openAs(item.owner)
		E3vl.TRUE
	}

	override def getGenAssetsBlock = Some(JsonGenAssetsBlock.customSingleModel(this))
}
