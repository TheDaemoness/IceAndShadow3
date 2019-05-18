package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.block.BMateria
import mod.iceandshadow3.compat.block.BCompatLogicBlock
import mod.iceandshadow3.compat.item.CRefItem

sealed abstract class BLogicBlock(dom: BDomain, name: String, mat: BMateria) extends BCompatLogicBlock(dom, name, mat) {
	dom.add(this)
}

abstract class BLogicBlockSimple(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	type StateDataType = BStateData
	override final def getDefaultStateData(variant: Int) = null
}

abstract class BLogicBlockComplex(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	//TODO: Manually generated class stub. For blocks with tile entities.
}
