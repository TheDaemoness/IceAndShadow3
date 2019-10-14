package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.base.LogicProvider
import mod.iceandshadow3.lib.compat.misc.TWUsageOnBlock
import net.minecraft.block.Block

abstract class BWBlockType
extends LogicProvider.Block {
	protected[compat] def asBlock(): Block

	override def getLogicPair = asBlock() match {
		case lp: LogicProvider.Block => lp.getLogicPair
		case _ => null
	}

	def typeForPlace(what: TWUsageOnBlock) = new WBlockState(asBlock().getStateForPlacement(what.expose))
	def typeDefault = new WBlockState(asBlock().getDefaultState)
}
