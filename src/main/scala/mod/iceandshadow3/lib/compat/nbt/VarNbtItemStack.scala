package mod.iceandshadow3.lib.compat.nbt

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.data.Var
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT

class VarNbtItemStack(name: String) extends Var[WItemStack](name) with TVarNbtCompound[WItemStack] {
	override def defaultVal = WItemStack.empty
	override def isDefaultValue(value: WItemStack) = value.isEmpty
	override protected def fromCompound(what: CompoundNBT) = Some(new WItemStack(ItemStack.read(what)))
	override protected def toTag(value: WItemStack) = value.expose().serializeNBT()
}
