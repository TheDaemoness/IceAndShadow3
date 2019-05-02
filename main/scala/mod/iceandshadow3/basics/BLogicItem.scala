package mod.iceandshadow3.basics

import mod.iceandshadow3.BDomain
import mod.iceandshadow3.compat.BLogic

abstract class BLogicItem(dom: BDomain, name: String) extends BLogic(dom, "item_"+name)  {
	def stackLimit(): Int = 64
}
