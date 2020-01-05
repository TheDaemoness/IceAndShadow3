package mod.iceandshadow3.lib.compat.util

import javax.annotation.Nullable
import mod.iceandshadow3.lib.base._
import net.minecraft.nbt.CompoundNBT

trait TWLogical[LogicType <: BLogic] {
	this: LogicProvider[LogicType] =>

	protected def exposeCompoundOrNull(): CompoundNBT
}
