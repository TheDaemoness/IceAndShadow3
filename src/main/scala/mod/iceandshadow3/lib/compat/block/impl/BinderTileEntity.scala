package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.LogicTileEntity
import mod.iceandshadow3.lib.util.collect.Binder

object BinderTileEntity extends Binder[LogicTileEntity, ATileEntityType] {
	def create(option: Option[LogicTileEntity]): ATileEntity = {
		if(option.isDefined) apply(option.get).create()
		else null
	}
}
