package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.lib.data.BVar
import net.minecraft.nbt.{CompoundNBT, INBT}

trait TVarNbt[T] {
	this: BVar[T] =>
	private[compat] final def readNbt(what: CompoundNBT): Option[T] = {
		val nullabletag = what.get(name)
		if(nullabletag == null) None
		else fromTag(nullabletag)
	}
	private[compat] final def writeNbt(what: CompoundNBT, value: T): Unit =
		what.put(name, toTag(value))
	protected def fromTag(what: INBT): Option[T]
	protected def toTag(value: T): INBT
}
