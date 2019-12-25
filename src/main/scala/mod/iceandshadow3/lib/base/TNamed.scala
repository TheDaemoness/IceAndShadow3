package mod.iceandshadow3.lib.base

import javax.annotation.Nonnull
import mod.iceandshadow3.lib.compat.WId

trait TNamed[+Id <: WId] {
	@Nonnull def id: Id
	def namespace: String = id.namespace
	def name: String = id.name
}
