package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicItemSingle
import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.multiverse.DomainPolis

class LIMoonstoneDust extends LogicItemSingle(DomainPolis, "moonstone_dust", 2, 512) {
	private val materiaQuery = BlockQueries.materia(Materias.moonstone_dust)
	override def handlerTickOwned(held: Boolean) = if(held) items => {
		val block = items.block
		if(block.isAir && !materiaQuery(block)) {
			if(block.place(DomainPolis.Blocks.moonstone_dust.toWBlockState)) items.degrade()
		}
	} else null
	override def getItemModelGen = None
}
object LIMoonstoneDust {
	val DUST_PER_ITEM = 32
}
