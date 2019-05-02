package mod.iceandshadow3.compat

import mod.iceandshadow3.basics._

import net.minecraftforge.registries.IForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistry
import net.minecraft.item.Item
import net.minecraft.block.Block

sealed class CRegistry[RegistryType <: IForgeRegistryEntry[RegistryType], IaSType <: BLogic](
		registry: IForgeRegistry[RegistryType]) {
	
	protected def register[AdapterType](logic: IaSType, entry: RegistryType): IaSType = {
		registry.register(entry)
		logic.secrets.asInstanceOf[SecretsLogic[IaSType,RegistryType]].add(entry)
		logic
	}
}

class CRegistryBlock(reg: IForgeRegistry[Block]) extends CRegistry[Block, BLogicBlock](reg) {
	def add(materia: BMateria) = {
		//TODO: All the basic materialed block variants.
		//Slab
		//Stair
	}
	def add(logic: BLogicBlock): BLogicBlock = {
		logic.secrets = new SecretsLogic[BLogicBlock,Block](logic)
		for(variant <- 0 to logic.countVariants()-1) register(logic, new ABlock(logic, variant))
		logic
	}
}

class CRegistryItem(reg: IForgeRegistry[Item]) extends CRegistry[Item, BLogicItem](reg) {
	def add(logic: BLogicItem): BLogicItem = {
		logic.secrets = new SecretsLogic[BLogicItem,Item](logic)
		for(variant <- 0 to logic.countVariants()-1) register(logic, new AItem(logic, variant))
		logic
	}
}