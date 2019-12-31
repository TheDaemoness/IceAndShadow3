package mod.iceandshadow3.lib.compat.file

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem}

abstract class BJsonGenModelItem(name: String) extends BJsonGenAsset(name) {
	final override def basePath = "models/item"
}
abstract class BJsonGenModelBlock(name: String) extends BJsonGenAsset(name) {
	final override def basePath = "models/block"
}