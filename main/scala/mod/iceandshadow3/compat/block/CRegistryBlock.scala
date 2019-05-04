package mod.iceandshadow3.compat.block

import mod.iceandshadow3.compat.CRegistry
import mod.iceandshadow3.compat.SecretsLogic
import net.minecraftforge.registries.IForgeRegistry

import mod.iceandshadow3.basics.BLogicBlock
import net.minecraft.block.Block

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