package mod.iceandshadow3.lib.compat.block.impl

import net.minecraft.state.IProperty

/** Represents one variable of a block's overall state. Similar to IProperty. */
sealed abstract class BBlockVar[T](val name: String) extends BinderBlockVar.TKey {
	type Underlying <: Comparable[Underlying]
	def size: Int
	def defaultValue: T

	protected def toUs(in: Underlying): T
	protected def toThem(in: T): Underlying
}

abstract class BBlockVarExisting[T, Original <: Comparable[Original]] protected[block] (ip: IProperty[Original])
extends BBlockVar[T](ip.getName) {
	override type Underlying = Original
	val size = ip.getAllowedValues.size()

	BinderBlockVar.add(this, WrappedIProperty[T, Original](ip, toUs, toThem))
}

abstract class BBlockVarNew[T](name: String) extends BBlockVar[T](name) {
	override type Underlying = Integer

	def fromString(value: String): Int
	def toString(value: Int): String //Can be implemented here, but let's keep things together now.

	BinderBlockVar.add(this, WrappedIProperty(new AProperty(this), toUs, toThem))
}
