package mod.iceandshadow3.basics.common

import mod.iceandshadow3.basics.{BDomain, BLogicBlockSimple}
import mod.iceandshadow3.compat.block.impl.BMateria

class LogicBlockOfMateria(dom: BDomain, mat: BMateria) extends BLogicBlockSimple(dom, mat.getName, mat) {
	//TODO: Block shapes.
}