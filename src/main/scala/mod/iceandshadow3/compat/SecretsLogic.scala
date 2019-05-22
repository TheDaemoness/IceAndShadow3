package mod.iceandshadow3.compat

import java.util.ArrayList

import mod.iceandshadow3.basics.util.TLogic

/** A mapping between BLogics and the Minecraft types they control the behaviors of.
 */
class SecretsLogic[Logic <: TLogic, McType <: Object](l: Logic) {
	private val flatmap = new ArrayList[McType](l.countVariants)
	protected[compat] def add(adapter: McType) = flatmap.add(adapter)
	protected[compat] def get(variant: Int) = flatmap.get(variant) //TODO: Null?
}