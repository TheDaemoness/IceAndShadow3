package mod.iceandshadow3.lib.compat.file

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem}

abstract class BJsonGenAsset(val name: String) {
	def apply: String
	def basePath: String
}

object BJsonGenAsset {
	def modelItemBlockDefault(logic: BLogic with TLogicWithItem, parentPath: String): BJsonGenModelItem =
		new BJsonGenModelItem(logic.name) {
			override def apply =
			"{\"parent\":\"iceandshadow3:block/"+parentPath+"\"}"
		}
	def modelItemBlockDefault(logic: BLogic with TLogicWithItem): BJsonGenModelItem =
		modelItemBlockDefault(logic, logic.name)

	def modelItemDefault(logic: BLogic with TLogicWithItem) = new BJsonGenModelItem(logic.name) {
		override def apply =
			"{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"iceandshadow3:item/"+logic.name+"\"}}"
	}

	def modelBlockCube(logic: BLogicBlock) = new BJsonGenModelBlock(logic.name) {
		override def apply =
			"{\"parent\":\"block/cube_all\",\"textures\":{\"all\":\"iceandshadow3:block/"+logic.name+"\"}}"
	}
	def modelBlockDeco(logic: BLogicBlock) = new BJsonGenModelBlock(logic.name) {
		override def apply =
			"{\"parent\":\"block/tinted_cross\",\"textures\":{\"cross\":\"iceandshadow3:block/"+logic.name+"\"}}"
	}
}
