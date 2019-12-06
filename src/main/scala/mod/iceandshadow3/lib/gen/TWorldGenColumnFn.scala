package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.data.BVar

object TWorldGenColumnFn {
	def noOp(dom: BDomain) = new TWorldGenColumnFn {
		override def domain = dom
		override def apply(v1: WorldGenColumn): Unit = ()
	}
}
trait TWorldGenColumnFn {
	def property[T](key: BVar[_]): Option[T] = None
	def domain: BDomain
	def apply(to: WorldGenColumn): Unit
}
