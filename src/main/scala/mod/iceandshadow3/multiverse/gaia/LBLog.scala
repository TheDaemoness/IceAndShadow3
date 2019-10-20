package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicBlockSimple
import mod.iceandshadow3.lib.block.CommonBlockVars
import mod.iceandshadow3.lib.compat.block.{Materia, WBlockState, WUsagePlace}
import mod.iceandshadow3.multiverse.DomainGaia

class LBLog(name: String, materia: Materia, val leafSupport: Int)
extends LogicBlockSimple(DomainGaia, name, materia) {
	override def variables = Array(CommonBlockVars.axis)

	override def toPlace(state: WBlockState, context: WUsagePlace) =
		state + (CommonBlockVars.axis, context.axis)

	override def getBlockstatesGen(variant: Int) = None
}
