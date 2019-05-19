package mod.iceandshadow3.compat.block

import mod.iceandshadow3.compat.CRegistry
import mod.iceandshadow3.compat.SecretsLogic
import net.minecraftforge.registries.IForgeRegistry
import mod.iceandshadow3.basics.BLogicBlock
import mod.iceandshadow3.compat.item.SCreativeTab
import net.minecraft.block.Block

class CRegistryBlock(reg: IForgeRegistry[Block]) extends CRegistry[Block, BLogicBlock](reg) {
	def add(logic: BLogicBlock): BLogicBlock = {
		logic.secrets = new SecretsLogic[BLogicBlock,Block](logic)
		for(variant <- 0 until logic.countVariants) register(logic, new ABlock(logic, variant))
		logic
	}
}