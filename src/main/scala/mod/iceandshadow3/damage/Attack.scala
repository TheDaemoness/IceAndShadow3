package mod.iceandshadow3.damage

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.util.TEffectSource

case class Attack(
	@Nullable source: TEffectSource,
	name: String,
	form: AttackForm,
	instances: (BDamage with TDmgTypeOmni)*
) {
	def this(name: String, form: AttackForm, instances: (BDamage with TDmgTypeOmni)*) =
		this(null, name, form, instances:_*)
}
