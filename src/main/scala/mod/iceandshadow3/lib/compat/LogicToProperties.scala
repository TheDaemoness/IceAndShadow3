package mod.iceandshadow3.lib.compat

import mod.iceandshadow3.lib.{BLogicBlock, BLogicItem}
import mod.iceandshadow3.lib.base.BLogicWithItem
import mod.iceandshadow3.lib.compat.item.impl.CreativeTab
import net.minecraft.block.Block
import net.minecraft.item.Item

private[compat] object LogicToProperties {

	def toPropertiesPartial(item: BLogicWithItem, variant: Int): Item.Properties = {
		val retval = new Item.Properties
		retval.maxStackSize(item.stackLimit(variant))
		retval.rarity(item.getDomain.tierToRarity(item.getTier(variant)).rarity)
		if (!item.isTechnical) retval.group(CreativeTab)
		retval
	}

	def toProperties(item: BLogicItem, variant: Int): Item.Properties = {
		val retval = toPropertiesPartial(item, variant)
		val damageLimit = item.damageLimit(variant)
		if (item.stackLimit(variant) == 1 && damageLimit > 0) retval.defaultMaxDamage(damageLimit)
		retval
	}

	def toProperties(block: BLogicBlock, variant: Int): Block.Properties = {
		val retval = Block.Properties.create(block.materia.mcmat)
		val materia = block.materia
		retval.hardnessAndResistance(materia.getBaseHardness, materia.getBaseBlastResist)
		retval.lightValue(materia.getBaseLuma)
		retval.slipperiness(materia.getSlipperiness)
		if (block.multipleOpacities) retval.variableOpacity
		if (block.randomlyUpdates.isDefined) retval.tickRandomly
		if (materia.isNonSolid) retval.doesNotBlockMovement
		retval.sound(materia.sound)
		//TODO: There's more.
		retval
	}
}
