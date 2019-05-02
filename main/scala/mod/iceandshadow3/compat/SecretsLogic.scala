package mod.iceandshadow3.compat

import mod.iceandshadow3.basics.TLogic
import java.util.ArrayList

class SecretsLogic[Logic <: TLogic, McType <: Object](l: Logic) {
	private val flatmap = new ArrayList[McType](l.countVariants)
	protected[compat] def add(adapter: McType) = flatmap.add(adapter)
	protected[compat] def get(variant: Int) = flatmap.get(variant) //TODO: Null?
}