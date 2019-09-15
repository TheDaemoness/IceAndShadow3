package mod.iceandshadow3.lib.compat.nbt

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.data.BVar
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT

class VarNbtItemStack(name: String) extends BVar[WItemStack](name) with TVarNbtCompound[WItemStack] {
	override def defaultVal = WItemStack.empty
	override protected def fromCompound(what: CompoundNBT) = Some(new WItemStack(ItemStack.read(what), null))
	override protected def toTag(value: WItemStack) = value.exposeItems().serializeNBT()
}
