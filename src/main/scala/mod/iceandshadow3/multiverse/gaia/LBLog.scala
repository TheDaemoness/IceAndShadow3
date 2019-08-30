package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.BLogicBlockSimple
import mod.iceandshadow3.lib.block.CommonBlockVars
import mod.iceandshadow3.lib.compat.block.{BMateria, WBlockState, WUsagePlace}
import mod.iceandshadow3.multiverse.DomainGaia

class LBLog(name: String, materia: BMateria, val leafSupport: Int)
extends BLogicBlockSimple(DomainGaia, name, materia) {
	override def variables = Array(CommonBlockVars.axis)

	override def toPlace(state: WBlockState, context: WUsagePlace) = {
		state + (CommonBlockVars.axis, context.axis)
	}
}
