package mod.iceandshadow3.damage

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.util.TEffectSource

case class Attack(
	@Nullable source: TEffectSource,
	name: String,
	form: AttackForm,
	instances: (BDamage with TDmgType)*
)
object Attack {
	def apply(name: String, form: AttackForm, instances: (BDamage with TDmgType)*): Attack =
		Attack(null, name, form, instances:_*)
}
