package mod.iceandshadow3.lib.compat.file

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.item.impl.BCompatLogicCommon

sealed abstract class BJsonAssetGen[-T <: BCompatLogicCommon] {
	def apply(logic: T, variant: Int): String
	def path: String
}
abstract class BJsonAssetGenItem[-T <: BCompatLogicCommon] extends BJsonAssetGen[T]{
	final override def path = "models/item"
}
abstract class BJsonAssetGenBlock extends BJsonAssetGen[BLogicBlock] {
	final override def path = "models/block"
}
abstract class BJsonAssetGenBlockstates extends BJsonAssetGen[BLogicBlock] {
	final override def path = "blockstates"
}
object BJsonAssetGen {
	val itemBlockDefault = new BJsonAssetGenItem[BLogicBlock] {
		override def apply(logic: BLogicBlock, variant: Int) =
			"{\"parent\":\"iceandshadow3:block/"+logic.getName(variant)+"\"}"
	}
	val itemDefault = new BJsonAssetGenItem[BCompatLogicCommon] {
		override def apply(logic: BCompatLogicCommon, variant: Int) =
			"{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"iceandshadow3:item/"+logic.getName(variant)+"\"}}"
	}
	val blockstatesDefault = new BJsonAssetGenBlockstates {
		override def apply(logic: BLogicBlock, variant: Int) =
			"{\"variants\":{\"\":{\"model\":\"iceandshadow3:block/"+logic.getName(variant)+"\"}}}"
	}
	val blockCube = new BJsonAssetGenBlock {
		override def apply(logic: BLogicBlock, variant: Int) =
			"{\"parent\":\"block/cube_all\",\"textures\":{\"all\":\"iceandshadow3:block/"+logic.getName(variant)+"\"}}"
	}
	val blockDeco = new BJsonAssetGenBlock {
		override def apply(logic: BLogicBlock, variant: Int) =
			"{\"parent\":\"block/tinted_cross\",\"textures\":{\"cross\":\"iceandshadow3:block/"+logic.getName(variant)+"\"}}"
	}
}
