package mod.iceandshadow3.lib.gen

import javax.annotation.Nullable
import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.data.BVar

abstract class BWorldGenColumnFn(@Nullable val domain: BDomain) extends (WorldGenColumn => Unit) {
	def property[T](key: BVar[_]): Option[T] = None
}
