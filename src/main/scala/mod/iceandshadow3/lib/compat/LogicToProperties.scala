package mod.iceandshadow3.lib.compat

import mod.iceandshadow3.lib.{BLogicBlock, BLogicItem}
import mod.iceandshadow3.lib.base.BLogicWithItem
import mod.iceandshadow3.lib.compat.item.impl.CreativeTab
import net.minecraft.block.Block
import net.minecraft.item.Item

private[compat] object LogicToProperties {

	def toPropertiesPartial(item: BLogicWithItem): Item.Properties = {
		val retval = new Item.Properties
		retval.maxStackSize(item.stackLimit)
		retval.rarity(item.domain.tierToRarity(item.tier).rarity)
		if (!item.isTechnical) retval.group(CreativeTab)
		retval
	}

	def toProperties(item: BLogicItem): Item.Properties = {
		val retval = toPropertiesPartial(item)
		val damageLimit = item.damageLimit
		if (item.stackLimit == 1 && damageLimit > 0) retval.defaultMaxDamage(damageLimit)
		retval
	}

	def toProperties(block: BLogicBlock): Block.Properties = {
		val retval = Block.Properties.create(block.materia.secrets.material)
		val materia = block.materia
		retval.hardnessAndResistance(materia.hardness, materia.resistance)
		retval.lightValue(materia.luma)
		retval.slipperiness(materia.slipperiness)
		if (block.multipleOpacities) retval.variableOpacity
		if (block.randomlyUpdates.isDefined) retval.tickRandomly
		if (!materia.solid) retval.doesNotBlockMovement
		retval.sound(materia.secrets.soundType)
		//TODO: There's more.
		retval
	}
}
