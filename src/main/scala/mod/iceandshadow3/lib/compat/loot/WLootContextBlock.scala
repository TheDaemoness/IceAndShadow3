package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.inventory.WInventory
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.world.TWWorldPlace
import mod.iceandshadow3.lib.spatial.{IPosBlock, IPositionalCoarse}
import net.minecraft.inventory.IInventory
import net.minecraft.world.storage.loot.{LootContext, LootParameters}

class WLootContextBlock(lootContext: LootContext) extends WLootContext(lootContext)
with TWWorldPlace with IPositionalCoarse {
	override def posCoarse = IPosBlock.wrap(lootContext.get(LootParameters.POSITION))
	def state = new WBlockState(lootContext.get(LootParameters.BLOCK_STATE))
	def tool = new WItemStack(lootContext.get(LootParameters.TOOL))
	def harvester = Option(lootContext.get(LootParameters.THIS_ENTITY)).map(CNVEntity.wrap)
	def blast = {
		val result = lootContext.get(LootParameters.EXPLOSION_RADIUS)
		if(result == null) 0f else result.floatValue()
	}
	def inventory = lootContext.get(LootParameters.BLOCK_ENTITY) match {
		case inv: IInventory => Some(new WInventory(inv))
		case _ => None
	}
}
