package mod.iceandshadow3.lib.data

import java.lang
import java.util.function.Consumer

import mod.iceandshadow3.lib.compat.nbt.TVarNbt

final class VarSet[Var <: BVar[_]](set: Set[Var]) extends Set[Var] {
	override def iterator = set.iterator
	def asJava = new lang.Iterable[Var] {
		import scala.jdk.CollectionConverters._
		override def iterator() = set.iterator.asJava
		override def forEach(consumer: Consumer[_ >: Var]): Unit = set.foreach(in => consumer.accept(in))
	}
	override def incl(elem: Var) = if(set.contains(elem)) this else new VarSet(set.incl(elem))
	override def excl(elem: Var) = if(!set.contains(elem)) this else new VarSet(set.excl(elem))
	override def contains(elem: Var) = set.contains(elem)
	override def canEqual(that: Any) = set.canEqual(that)
	override def equals(that: Any) = set.equals(that)
	override def size = set.size
	override def knownSize = set.knownSize
}
object VarSet {
	type WithNbt[T] = VarSet[BVar[T] with TVarNbt[T]]
	def empty[Var <: BVar[_]] = new VarSet(Set.empty[Var])
	def apply[Var <: BVar[_]](options: Var*): VarSet[Var] = new VarSet(Set.from(options))
}
