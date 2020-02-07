package mod.iceandshadow3.lib.compat.file

import com.google.gson.JsonObject
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.misc.CubeValues

abstract class JsonGenModelBlock(name: String) extends JsonGen(name) {
	final override def basePath = "models/block"
}
object JsonGenModelBlock {
	def simpleModel(parent: String, textureMap: Map[String, String]) = {
		val obj = new JsonObject
		obj.addProperty("parent", parent)
		val textures = new JsonObject
		for((id, value) <- textureMap) textures.addProperty(id, IaS3.MODID+":block/"+value)
		obj.add("textures", textures)
		obj.toString
	}
	def cube(name: String, textures: CubeValues[String]): JsonGenModelBlock = {
		if(textures.equalFaces) new JsonGenModelBlock(name) {
			override def apply = simpleModel("block/cube_all", Map(
				("all", textures.front))
			)
		} else if(textures.equalSides) {
			if (textures.equalEnds) new JsonGenModelBlock(name) {
				override def apply = simpleModel("block/cube_column", Map(
					("end", textures.top),
					("side", textures.front)
				))
			} else new JsonGenModelBlock(name) {
				override def apply = simpleModel("block/cube_bottom_top", Map(
					("top", textures.top),
					("side", textures.front),
					("bottom", textures.bottom)
				))
			}
		} else new JsonGenModelBlock(name) {
			override def apply = simpleModel("block/cube", Map(
				("particles", textures.front),
				("up", textures.top),
				("down", textures.bottom),
				("west", textures.left),
				("east", textures.right),
				("south", textures.front),
				("north", textures.back)
			))
		}
	}

	def cube(logic: BLogicBlock): JsonGenModelBlock = cube(logic.name, CubeValues.builder(logic.name).result)

	def deco(logic: BLogicBlock) = new JsonGenModelBlock(logic.name) {
		override def apply =
			"{\"parent\":\"block/tinted_cross\",\"textures\":{\"cross\":\"iceandshadow3:block/"+logic.name+"\"}}"
	}
}