package mod.iceandshadow3.lib.compat

import mod.iceandshadow3.lib.compat.client.impl.BinderParticle
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect

object Binders {
	def prepopulate(): Unit = {
		BinderStatusEffect.populate()
		BinderParticle.populate()
	}
}
