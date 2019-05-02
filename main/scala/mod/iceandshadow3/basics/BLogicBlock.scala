package mod.iceandshadow3.basics

import mod.iceandshadow3.BDomain
import mod.iceandshadow3.compat.BMateria
import mod.iceandshadow3.compat.BMateriaLogic
import mod.iceandshadow3.compat.CRefItem
import mod.iceandshadow3.compat.HarvestMethod

abstract class BLogicBlock(dom: BDomain, name: String, mat: BMateria) extends BMateriaLogic(dom, "block_"+name, mat) {
	def isToolClassEffective(method: HarvestMethod): Boolean =
		this.getMateria().isToolClassEffective(method)
}
