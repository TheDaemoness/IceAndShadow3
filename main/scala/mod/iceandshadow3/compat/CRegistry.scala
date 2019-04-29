package mod.iceandshadow3.compat

import mod.iceandshadow3.basics._

import net.minecraftforge.registries.IForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistry
import net.minecraft.item.Item
import net.minecraft.block.Block

sealed class CRegistry[RegistryType <: IForgeRegistryEntry[RegistryType], IaSType <: Object](protected val registry: IForgeRegistry[RegistryType]) {
	protected def register(
			logic: IaSType,
			adapterclass: Class[_ <: RegistryType]) =
		registry.register(adapterclass.getConstructor(logic.getClass()).newInstance(logic));
}

class CRegistryBlock(reg: IForgeRegistry[Block]) extends CRegistry[Block, BlockLogic](reg) {
	def make(materia: BMateria) = {
		//TODO: All the basic materialed block variants.
		//Slab
		//Stair
	}
	def make(registry: IForgeRegistry[Block], logic: BlockLogic) =
		if(logic.countVariants() == 1)
		register(logic, classOf[ABlock]) else
		register(logic, classOf[ABlockMulti])
	
}

class CRegistryItem(reg: IForgeRegistry[Item]) extends CRegistry[Item, ItemLogic](reg) {
	def make(registry: IForgeRegistry[Item], logic: ItemLogic) =
		register(logic, classOf[AItem])
}