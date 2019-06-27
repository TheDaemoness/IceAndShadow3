package mod.iceandshadow3.lib.compat.util

trait IWrapper[T] {
	def isAny(queries: (T => Boolean)*): Boolean
	def isAll(queries: (T => Boolean)*): Boolean
}
