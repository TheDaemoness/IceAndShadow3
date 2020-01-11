package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.compat.loot.{Loot, LootBuilder, WLootContextBlock}
import mod.iceandshadow3.multiverse.DomainGaia

class LBMoonstone(id: String) extends LogicBlock(DomainGaia, id, Materias.moonstone) {
	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit = {
		what.addOne(Loot.silktouch(this).orElse(Loot.of(DomainGaia.Items.moonstone.toWItemType, 8.5f)))
	}
}
