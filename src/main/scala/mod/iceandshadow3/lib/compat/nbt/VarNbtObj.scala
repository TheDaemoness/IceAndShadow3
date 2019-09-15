package mod.iceandshadow3.lib.compat.nbt

import mod.iceandshadow3.lib.data.{BDataTree, BVar, IDataTreeRW}
import net.minecraft.nbt.INBT

/** Used to support objects that are IDataTreeRW for which it doesn't make sense to write a new VarNbt */
class VarNbtObj[T <: IDataTreeRW[_ <: BDataTree[_]]](name: String, factory: => T)
extends BVar[T](name) with TVarNbt[T] {
	override def defaultVal = factory
	final override protected def fromTag(what: INBT) = {
		val nova = factory
		if(nova.exposeDataTree().fromNBT(what)) Some(nova) else None
	}
	override protected def toTag(value: T) = value.exposeDataTree().toNBT
}
