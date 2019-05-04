package mod.iceandshadow3.basics

import mod.iceandshadow3.BDomain
import mod.iceandshadow3.compat.block.BMateria
import mod.iceandshadow3.compat.block.BCompatLogicBlock
import mod.iceandshadow3.compat.item.CRefItem

abstract class BLogicBlock(dom: BDomain, name: String, mat: BMateria) extends BCompatLogicBlock(dom, name, mat) {
	def isToolClassEffective(method: HarvestMethod): Boolean =
		this.getMateria().isToolClassEffective(method)
}
