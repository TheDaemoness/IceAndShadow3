package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.util.collect.Binder

object BinderBlockVar extends Binder[VarBlock[_], WIProperty[_, _]] {
	def get[T](which: VarBlock[T]): WIProperty[T, _] =
		apply(which).asInstanceOf[WIProperty[T, _]]
}
