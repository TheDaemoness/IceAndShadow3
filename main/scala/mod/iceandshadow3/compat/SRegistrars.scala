package mod.iceandshadow3.compat

import mod.iceandshadow3.basics._

import net.minecraftforge.registries.IForgeRegistry
import net.minecraft.item.Item
import net.minecraft.block.Block

sealed class Registrar[RegistryType <: net.minecraftforge.registries.IForgeRegistryEntry[RegistryType], IaSType <: Object] {
	protected def register(
			registry: IForgeRegistry[RegistryType],
			logic: IaSType,
			adapterclass: Class[_ <: RegistryType]) =
		registry.register(adapterclass.getConstructor(logic.getClass()).newInstance(logic));
}

object SRegistrarBlock extends Registrar[Block, BlockLogic] {
	def make(registry: IForgeRegistry[Block], materia: BMateria) = {
		//TODO: All the basic materialed block variants.
		//Slab
		//Stair
	}
	def make(registry: IForgeRegistry[Block], logic: BlockLogic) =
		if(logic.countVariants() == 1)
		register(registry, logic, classOf[ABlock]) else
		register(registry, logic, classOf[ABlockMulti])
	
}

object SRegistrarItem extends Registrar[Item, ItemLogic] {
	def make(registry: IForgeRegistry[Item], logic: ItemLogic) =
		register(registry, logic, classOf[AItem])
}