package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.base.ProviderLogic
import mod.iceandshadow3.lib.compat.id.WIdBlock
import mod.iceandshadow3.lib.compat.misc.TWUsageOnBlock
import net.minecraft.block.Block

abstract class WBlockType
extends ProviderLogic.Block {
	protected[compat] def asBlock(): Block

	override def getLogic = asBlock() match {
		case lp: ProviderLogic.Block => lp.getLogic
		case _ => null
	}

	def typeForPlace(what: TWUsageOnBlock) = new WBlockState(asBlock().getStateForPlacement(what.expose))
	def typeDefault = new WBlockState(asBlock().getDefaultState)

	override def id: WIdBlock = new WIdBlock(asBlock().getRegistryName)
}
