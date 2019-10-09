package mod.iceandshadow3.damage

import mod.iceandshadow3.damage.impl.ADamageSource
import mod.iceandshadow3.lib.compat.entity.WEntity

final class Attack private(
	val sourceTrue: Option[WEntity],
	val sourceImmediate: Option[WEntity],
	val name: String,
	val form: AttackForm,
	val instances: (BDamage with TDmgType)*
) extends (WEntity => Boolean) {
	def apply(who: WEntity): Boolean =
		ADamageSource.buildAndWrap(this, who, 1f)(who)
	def *(multiplier: Float): WEntity => Boolean =
		victim => ADamageSource.buildAndWrap(this, victim, multiplier)(victim)
}
object Attack {
	def apply(
		name: String, form: AttackForm, instances: (BDamage with TDmgType)*
	): Attack =
		new Attack(None, None, name, form, instances:_*)
	def apply(
		source: WEntity,
		name: String, form: AttackForm, instances: (BDamage with TDmgType)*
	): Attack = {
		val wrapped = Some(source)
		new Attack(wrapped, wrapped, name, form, instances:_*)
	}
	def apply(
		sourceTrue: WEntity, sourceIndirect: WEntity,
		name: String, form: AttackForm, instances: (BDamage with TDmgType)*
	): Attack =
		new Attack(Some(sourceTrue), Some(sourceIndirect), name, form, instances:_*)
}
