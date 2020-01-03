package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.compat.block.{CommonBlockVars, Materia, WBlockState, WUsagePlace}
import mod.iceandshadow3.lib.data.VarSet
import mod.iceandshadow3.multiverse.DomainGaia

class LBLog(name: String, materia: Materia, val leafSupport: Int)
extends LogicBlock(DomainGaia, name, materia) {
	override val variables = VarSet(CommonBlockVars.axis)

	override def toPlace(state: WBlockState, context: WUsagePlace) =
		state + (CommonBlockVars.axis, context.axis)

	//TODO: Log asset gen.
	override def getGenAssetsBlock = None
}
