package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.util.collect.Binder

object BinderBlockVar extends Binder[BVarBlock[_], WrappedIProperty[_, _]] {
	def get[T](which: BVarBlock[T]): WrappedIProperty[T, _] =
		apply(which).asInstanceOf[WrappedIProperty[T, _]]
}
