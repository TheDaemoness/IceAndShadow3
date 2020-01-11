package mod.iceandshadow3.lib.compat.file

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.base.{LogicCommon, TLogicWithItem}

abstract class JsonGen(val name: String) {
	def apply: String
	def basePath: String
}

object JsonGen {
	def modelItemBlockDefault(logic: LogicCommon with TLogicWithItem, parentPath: String): JsonGenModelItem =
		new JsonGenModelItem(logic.name) {
			override def apply =
			"{\"parent\":\"iceandshadow3:block/"+parentPath+"\"}"
		}
	def modelItemBlockDefault(logic: LogicCommon with TLogicWithItem): JsonGenModelItem =
		modelItemBlockDefault(logic, logic.name)

	def modelItemDefault(logic: LogicCommon with TLogicWithItem) = new JsonGenModelItem(logic.name) {
		override def apply =
			"{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"iceandshadow3:item/"+logic.name+"\"}}"
	}
}
