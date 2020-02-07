package mod.iceandshadow3.lib.data

import java.lang
import java.util.function.Consumer

import mod.iceandshadow3.lib.compat.nbt.TVarNbt

final class VarSet[VarType <: Var[_]](set: Set[VarType]) extends Set[VarType] {
	override def iterator = set.iterator
	def asJava = new lang.Iterable[VarType] {
		import scala.jdk.CollectionConverters._
		override def iterator() = set.iterator.asJava
		override def forEach(consumer: Consumer[_ >: VarType]): Unit = set.foreach(in => consumer.accept(in))
	}
	override def incl(elem: VarType) = if(set.contains(elem)) this else new VarSet(set.incl(elem))
	override def excl(elem: VarType) = if(!set.contains(elem)) this else new VarSet(set.excl(elem))
	override def contains(elem: VarType) = set.contains(elem)
	override def canEqual(that: Any) = set.canEqual(that)
	override def equals(that: Any) = set.equals(that)
	override def size = set.size
	override def knownSize = set.knownSize
}
object VarSet {
	type WithNbt[T] = VarSet[Var[T] with TVarNbt[T]]
	def empty[VarType <: Var[_]] = new VarSet(Set.empty[VarType])
	def apply[VarType <: Var[_]](options: VarType*): VarSet[VarType] = new VarSet(Set.from(options))
}
