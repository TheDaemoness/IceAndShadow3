package mod.iceandshadow3.lib.compat.nbt

import mod.iceandshadow3.lib.compat.nbt.NbtVarMap.ValueType
import mod.iceandshadow3.lib.data.{BVar, VarSet}
import net.minecraft.nbt.CompoundNBT

import scala.collection.mutable

object NbtVarMap {
	val orderingKeys = BVar.ordering
	val ordering = new Ordering[ValueType[_]] {
		override def compare(x: ValueType[_], y: ValueType[_]) = orderingKeys.compare(x.key, y.key)
	}

	@specialized final class ValueType[T](
		val key: BVar[T] with TVarNbt[T],
		private var value: T
	) {
		def get = value
		def update(what: T): this.type = {
			value = what
			this
		}
		def loadFrom(nbt: CompoundNBT): Unit = {
			val tmp = key.readNbt(nbt)
			if(tmp.isDefined) value = tmp.get
			else if(!key.isDefaultValue(value)) value = key.defaultVal
		}
		def writeTo(nbt: CompoundNBT): Unit = {
			if(!key.isDefaultValue(value)) key.writeNbt(nbt, value)
		}
		//Originally there was a delta optimization here, but I'm pretty sure MC netcode won't trivially allow it.
		//Further R&D necessary.
	}
	def newValue[T](key: BVar[T] with TVarNbt[T]) = new ValueType[T](key, key.defaultVal)
}

/** Java-friendly Nbt-ready variable map. */
final class NbtVarMap(keys: VarSet.WithNbt[_]) {
	private val map = new Iterable[ValueType[_]] {
		private val underlying: mutable.ArraySeq[ValueType[_]] = {
			val arrayseq = mutable.ArraySeq.fill[ValueType[_]](keys.size)(null)
			var i = 0
			val it = keys.iterator
			while(it.hasNext) {
				arrayseq.update(i, NbtVarMap.newValue(it.next()))
				i += 1
			}
			arrayseq.sortInPlace()(NbtVarMap.ordering)
			arrayseq
		}
		def apply[T](key: BVar[T]): ValueType[T] = {
			var left =  0
			var right = underlying.size - 1
			while(left <= right) {
				val idx = (left + right)/2
				val value = underlying(idx)
				val compare = NbtVarMap.orderingKeys.compare(value.key, key)
				if(compare < 0) left = idx + 1
				else if(compare > 0) right = idx - 1
				else return value.asInstanceOf[ValueType[T]]
			}
			throw new NoSuchElementException(s"Attempted to fetch $key from $this")
		}
		override def iterator = underlying.iterator
		override def size = underlying.size
		override def knownSize = size
	}

	def apply[T](what: BVar[T]): T = map.apply(what).get
	def update[T](what: BVar[T], valueNew: T): this.type = {
		map(what).update(valueNew)
		this
	}

	private[compat] def writeTo(what: CompoundNBT): this.type = {
		map.foreach(_.writeTo(what))
		this
	}
	private[compat] def loadFrom(what: CompoundNBT): this.type  = {
		map.foreach(_.loadFrom(what))
		this
	}
}

