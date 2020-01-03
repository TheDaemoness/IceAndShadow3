package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.data.BVar
import net.minecraft.state.IProperty

/** Represents one variable of a block's overall state. Similar to IProperty. */
sealed abstract class BVarBlock[T](name: String, override val defaultVal: T)
extends BVar[T](name) with BinderBlockVar.TKey {
	type Underlying <: Comparable[Underlying]
	def size: Int

	protected def toUs(in: Underlying): T
	protected def toThem(in: T): Underlying
	def unapply(what: WBlockState): Option[T] = what.apply(this)
}

abstract class BVarBlockExisting[T, Original <: Comparable[Original]] protected[block] (
	ip: IProperty[Original],
	defaultVal: T
) extends BVarBlock[T](ip.getName, defaultVal) {
	override type Underlying = Original
	val size = ip.getAllowedValues.size()

	BinderBlockVar.add(this, WIProperty[T, Original](ip, toUs, toThem))
}

abstract class BVarBlockNew[T](name: String, defaultVal: T) extends BVarBlock[T](name, defaultVal) {
	override type Underlying = Integer

	def fromString(value: String): Int
	def toString(value: Int): String //Can be implemented here, but let's keep things together now.

	BinderBlockVar.add(this, WIProperty(new AProperty(this), toUs, toThem))
}
