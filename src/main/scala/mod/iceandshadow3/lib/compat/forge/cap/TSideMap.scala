package mod.iceandshadow3.lib.compat.forge.cap

import javax.annotation.{Nonnull, Nullable}
import net.minecraft.util.Direction
import net.minecraftforge.common.util.LazyOptional

trait TSideMap[+T]  {
	def existsCenter = center != null
	def exists(@Nonnull side: Direction) = apply(side) != null
	def center: T
	def apply(side: Direction): T
}
object TSideMap {
	type Factory[In, T] = In => TSideMap[LazyOptional[T]]
	def all[In, T](factory: In => T): Factory[In, T] = (in: In) => new TSideMap[LazyOptional[T]] {
		private final val t: T = factory(in)
		private final val lazyOpt = LazyOptional.of[T](() => t)
		override def existsCenter = true
		override def exists(side: Direction) = true
		override def center = lazyOpt
		override def apply(v1: Direction) = lazyOpt
	}
	//TODO: More variants!
}
