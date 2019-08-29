package mod.iceandshadow3.lib.subtype

import mod.iceandshadow3.lib.compat.block.BMateria
import mod.iceandshadow3.lib.{BDomain, BLogicBlockSimple}

class LogicBlockOfMateria(dom: BDomain, mat: BMateria) extends BLogicBlockSimple(dom, mat.getName, mat) {
	//TODO: Block shapes.
}