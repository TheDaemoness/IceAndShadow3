package mod.iceandshadow3.lib.compat.nbt

import mod.iceandshadow3.lib.data.BVar
import net.minecraft.nbt.{CompoundNBT, INBT}

trait TVarNbtCompound[T] extends TVarNbt[T] {
	this: BVar[T] =>
	override protected def fromTag(what: INBT) =
		if(what.getId == NbtTagUtils.ID_COMPOUND) fromCompound(what.asInstanceOf[CompoundNBT]) else None
	protected def fromCompound(what: CompoundNBT): Option[T]
}
