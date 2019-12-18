package mod.iceandshadow3.lib.compat.file

import mod.iceandshadow3.lib.{BLogicBlock, LogicBlock}
import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem}

sealed abstract class BJsonAssetGen[-T <: BLogic] {
	def apply(logic: T): String
	def path: String
}
abstract class BJsonAssetGenItem[-T <: BLogic with TLogicWithItem] extends BJsonAssetGen[T]{
	final override def path = "models/item"
}
abstract class BJsonAssetGenBlock extends BJsonAssetGen[BLogicBlock] {
	final override def path = "models/block"
}
abstract class BJsonAssetGenBlockstates extends BJsonAssetGen[BLogicBlock] {
	final override def path = "blockstates"
}
object BJsonAssetGen {
	val itemBlockDefault = new BJsonAssetGenItem[LogicBlock] {
		override def apply(logic: LogicBlock) =
			"{\"parent\":\"iceandshadow3:block/"+logic.name+"\"}"
	}
	val itemDefault = new BJsonAssetGenItem[BLogic with TLogicWithItem] {
		override def apply(logic: BLogic with TLogicWithItem) =
			"{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"iceandshadow3:item/"+logic.name+"\"}}"
	}
	val blockstatesDefault = new BJsonAssetGenBlockstates {
		override def apply(logic: BLogicBlock) =
			"{\"variants\":{\"\":{\"model\":\"iceandshadow3:block/"+logic.name+"\"}}}"
	}
	val blockCube = new BJsonAssetGenBlock {
		override def apply(logic: BLogicBlock) =
			"{\"parent\":\"block/cube_all\",\"textures\":{\"all\":\"iceandshadow3:block/"+logic.name+"\"}}"
	}
	val blockDeco = new BJsonAssetGenBlock {
		override def apply(logic: BLogicBlock) =
			"{\"parent\":\"block/tinted_cross\",\"textures\":{\"cross\":\"iceandshadow3:block/"+logic.name+"\"}}"
	}
}
