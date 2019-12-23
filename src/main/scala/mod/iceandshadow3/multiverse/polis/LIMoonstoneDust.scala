package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicItemSingle
import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.lib.compat.recipe.ECraftingType
import mod.iceandshadow3.multiverse.{DomainGaia, DomainPolis}

class LIMoonstoneDust extends LogicItemSingle(DomainPolis, "moonstone_dust", 512, 2) {
	private val materiaQuery = BlockQueries.materia(Materias.moonstone_dust)
	override def handlerTickOwned(held: Boolean) = if(held) items => {
		val block = items.block
		if(block.isAir && !materiaQuery(block)) {
			if(block.place(DomainPolis.Blocks.moonstone_dust.toWBlockState)) items.degrade()
		}
	} else null
	override def getItemModelGen = None

	Registrar.addRecipeCallback(s"craft.$name.crush", name => {
		ECraftingType.CRAFT_SHAPELESS(name,
			ECraftingType.About({
				val stack = this.toWItemStack
				stack.setDamage(stack.getDamageMax - LIMoonstoneDust.DUST_PER_ITEM)
				stack
			}),
			DomainGaia.Items.moonstone.toWItemType
		)
	})
}
object LIMoonstoneDust {
	val DUST_PER_ITEM = 32
}
