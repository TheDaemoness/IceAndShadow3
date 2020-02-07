package mod.iceandshadow3.lib

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.block.impl.BinderTileEntity
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.forge.cap.{InventoryAccess, TSideMap}
import mod.iceandshadow3.lib.compat.id.WId
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.nbt.{NbtVarMap, TVarNbt}
import mod.iceandshadow3.lib.data.{Var, VarSet}

class LogicTileEntity(override val name: String, val variables: VarSet.WithNbt[_])
extends TNamed[WId] with BinderTileEntity.TKey {
	final val id = new WId(IaS3.MODID, name)

	val itemCapacity = 0
	def onLoad(data: NbtVarMap): Unit = ()
	def onUnload(data: NbtVarMap): Unit = ()
	def isUsableBy(who: WEntityPlayer) = true
	def canStore(slot: Int, stack: WItemStack) = true
	def syncInventoryOnLoad = false
	def handlerItem: InventoryAccess.SidedFactory = TSideMap.all(InventoryAccess.raw)
}
object LogicTileEntity {
	def apply(name: String, vars: (Var[_] with TVarNbt[_])*) = new LogicTileEntity(name, VarSet(vars:_*))
	val optionNone: Option[LogicTileEntity] = None
}
