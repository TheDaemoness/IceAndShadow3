package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.BMateria
import mod.iceandshadow3.compat.BBlockLogic
import mod.iceandshadow3.compat.CRefItem
import mod.iceandshadow3.compat.HarvestMethod

class BlockLogic(mat: BMateria) extends BBlockLogic(mat) {
	def isToolClassEffective(method: HarvestMethod): Boolean =
		this.getMateria().isToolClassEffective(method)
}
trait IBlockLogicProvider {
	def getBlockLogic(): BlockLogic
	def maxVariants(): Int
}