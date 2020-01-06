package mod.iceandshadow3.lib

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.WId
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.nbt.{NbtVarMap, TVarNbt}
import mod.iceandshadow3.lib.data.{BVar, VarSet}

class LogicTileEntity(override val name: String, val variables: VarSet.WithNbt[_]) extends TNamed[WId] {
	def onLoad(data: NbtVarMap): Unit = ()
	def onUnload(data: NbtVarMap): Unit = ()
	val itemCapacity = 0
	def isUsableBy(who: WEntityPlayer) = true
	def canStore(slot: Int, stack: WItemStack) = true
	final val id = new WId(IaS3.MODID, name)
}
object LogicTileEntity {
	def apply(name: String, vars: (BVar[_] with TVarNbt[_])*) = new LogicTileEntity(name, VarSet(vars:_*))
	val optionNone: Option[LogicTileEntity] = None
}
