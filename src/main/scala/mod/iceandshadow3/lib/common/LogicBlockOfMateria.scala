package mod.iceandshadow3.lib.common

import mod.iceandshadow3.lib.compat.block.Materia
import mod.iceandshadow3.lib.{BDomain, LogicBlockSimple}

class LogicBlockOfMateria(dom: BDomain, mat: Materia) extends LogicBlockSimple(dom, mat.name, mat) {
	//TODO: Block shapes.
}