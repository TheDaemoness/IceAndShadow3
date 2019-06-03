package mod.iceandshadow3.compat.block

import mod.iceandshadow3.basics.BLogicBlock
import mod.iceandshadow3.compat.{WRegistry, SecretsLogic}
import net.minecraft.block.Block
import net.minecraftforge.registries.IForgeRegistry

class WRegistryBlock(reg: IForgeRegistry[Block]) extends WRegistry[Block, BLogicBlock](reg) {
	def add(logic: BLogicBlock): BLogicBlock = {
		logic.secrets = new SecretsLogic[BLogicBlock,Block](logic)
		for(variant <- 0 until logic.countVariants) register(logic, new ABlock(logic, variant))
		logic
	}
}