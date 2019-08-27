package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.util.collect.BinderEarly

object BinderBlockVar extends BinderEarly[BBlockVar[_], WrappedIProperty[_, _]] {
	def get[T](which: BBlockVar[T]): WrappedIProperty[T, _] =
		apply(which).asInstanceOf[WrappedIProperty[T, _]]
}
