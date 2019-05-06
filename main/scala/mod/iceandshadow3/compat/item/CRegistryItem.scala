package mod.iceandshadow3.compat.item

import mod.iceandshadow3.basics.BLogicBlock
import mod.iceandshadow3.compat.CRegistry
import mod.iceandshadow3.compat.SecretsLogic
import net.minecraftforge.registries.IForgeRegistry

import mod.iceandshadow3.basics.BLogicItem
import net.minecraft.item.Item

class CRegistryItem(reg: IForgeRegistry[Item]) extends CRegistry[Item, BLogicItem](reg) {
	def add(logic: BLogicItem): BLogicItem = {
		logic.secrets = new SecretsLogic[BLogicItem,Item](logic)
		for(variant <- 0 to logic.countVariants()-1) register(logic, new AItem(logic, variant))
		logic
	}
	def add(logic: BLogicBlock): BLogicBlock = {
		//Do NOT change logic.secrets in this variant! This is for adding ItemBlocks only!
		for(variant <- 0 to logic.countVariants()-1) register(logic, new AItemBlock(logic, variant))
		logic
	}
}