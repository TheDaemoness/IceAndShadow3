package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemSingle
import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.multiverse.DomainGaia

class LIMoonstoneDust extends LogicItemSingle(DomainGaia, "moonstone_dust", 512, 2) {
	private val materiaQuery = BlockQueries.materia(Materias.moonstone_dust)
	override def handlerTickOwned(variant: Int, held: Boolean): WItemStackOwned[WEntity] => Unit = if(held) items => {
		val block = items.block
		if(block.isAir && !materiaQuery(block)) {
			if(block.place(DomainGaia.Blocks.moonstone_dust.asWBlockState())) items.degrade()
		}
	} else null
	override def getItemModelGen(variant: Int) = None
}
object LIMoonstoneDust {
	val DUST_PER_ITEM = 32
}
