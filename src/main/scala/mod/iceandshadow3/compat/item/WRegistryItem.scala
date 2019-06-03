package mod.iceandshadow3.compat.item

import mod.iceandshadow3.basics.{BLogicBlock, BLogicItem}
import mod.iceandshadow3.compat.{WRegistry, SecretsLogic}
import net.minecraft.item.Item
import net.minecraftforge.registries.IForgeRegistry

class WRegistryItem(reg: IForgeRegistry[Item]) extends WRegistry[Item, BLogicItem](reg) {
	def add(logic: BLogicItem): BLogicItem = {
		logic.secrets = new SecretsLogic[BLogicItem,Item](logic)
		for(variant <- 0 until logic.countVariants) register(logic, new AItem(logic, variant))
		logic
	}
	def add(logic: BLogicBlock): BLogicBlock = {
		//Do NOT change logic.secrets in this variant! This is for adding ItemBlocks only!
		for(variant <- 0 until logic.countVariants) register(logic, new AItemBlock(logic, variant))
		logic
	}
}