package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.{LogicBlock, LogicTileEntity}
import mod.iceandshadow3.lib.compat.block.{Materia, WBlockRef}
import mod.iceandshadow3.lib.compat.container.WContainerSource
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.data.VarSet
import mod.iceandshadow3.multiverse.DomainPolis

class LBBinStorage(name: String, mat: Materia) extends LogicBlock(DomainPolis, name, mat) {
	override val tileEntity = Some(new LogicTileEntity("bin_storage", VarSet.empty) {
		override val itemCapacity = 27
	})

	override def onUsed(us: WBlockRef, item: WItemStackOwned[WEntityPlayer]) = {
		us.container().openAs(item.owner)
		true
	}
	override def container(us: WBlockRef) = WContainerSource.chestlike(us, this.id.translationKey)
}
