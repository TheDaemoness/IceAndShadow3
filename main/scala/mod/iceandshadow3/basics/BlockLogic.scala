package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.BMateria
import mod.iceandshadow3.compat.CRefItem
import mod.iceandshadow3.compat.HarvestMethod

class BlockLogic(protected val mat: BMateria) {
	trait IProvider {
		def getBlockLogic(): BlockLogic
	}
	
	def getMateria(): BMateria = mat
}