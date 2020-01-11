package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.data.Var
import net.minecraft.state.IProperty

/** Represents one variable of a block's overall state. Similar to IProperty. */
sealed abstract class VarBlock[T](name: String, override val defaultVal: T)
extends Var[T](name) with BinderBlockVar.TKey {
	type Underlying <: Comparable[Underlying]
	def size: Int

	protected def toUs(in: Underlying): T
	protected def toThem(in: T): Underlying
	def unapply(what: WBlockState): Option[T] = what.apply(this)
}

abstract class VarBlockExisting[T, Original <: Comparable[Original]] protected[block] (
	ip: IProperty[Original],
	defaultVal: T
) extends VarBlock[T](ip.getName, defaultVal) {
	override type Underlying = Original
	val size = ip.getAllowedValues.size()

	BinderBlockVar.add(this, WIProperty[T, Original](ip, toUs, toThem))
}

abstract class VarBlockNew[T](name: String, defaultVal: T) extends VarBlock[T](name, defaultVal) {
	override type Underlying = Integer

	def fromString(value: String): Int
	def toString(value: Int): String //Can be implemented here, but let's keep things together now.

	BinderBlockVar.add(this, WIProperty(new AProperty(this), toUs, toThem))
}
