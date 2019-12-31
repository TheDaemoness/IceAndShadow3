package mod.iceandshadow3.lib.common.model

import com.google.gson.JsonObject
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.file.{BJsonGenAssetsBlock, BJsonGenModelBlock}
import mod.iceandshadow3.lib.misc.Column2Values

class JsonGenAssetsBlockSlab(
	logic: BLogicBlock,
	bottom: BJsonGenModelBlock, top: BJsonGenModelBlock, double: BJsonGenModelBlock
) extends BJsonGenAssetsBlock(logic) {
	//Theoretically we can easily support more diverse textures, but let's keep it simple.
	def this(logic: BLogicBlock, textures: Column2Values[String]) = this(logic,
		JsonGenAssetsBlockSlab.defaultBottom(logic, textures),
		JsonGenAssetsBlockSlab.defaultTop(logic, textures),
		JsonGenAssetsBlockSlab.defaultDouble(logic, textures)
	)
	override val models = Seq(bottom, top, double)
	override val apply = {
		import scala.util.chaining._
		val retval = new JsonObject
		retval.add("variants", new JsonObject().tap(variants => {
			variants.add("type=bottom", JsonGenAssetsBlockSlab.modelObj(bottom))
			variants.add("type=top", JsonGenAssetsBlockSlab.modelObj(top))
			variants.add("type=double", JsonGenAssetsBlockSlab.modelObj(double))
		}))
		retval.toString
	}
}
object JsonGenAssetsBlockSlab {
	private def modelObj(what: BJsonGenModelBlock) = {
		val retval = new JsonObject
		retval.addProperty("model", s"iceandshadow3:block/${what.name}")
		retval
	}
	private def modelString(parent: String, tex: Column2Values[String]) = BJsonGenModelBlock.simpleModel(parent, Map(
		("bottom", tex.end),
		("top", tex.end),
		("side", tex.side),
	))
	def defaultBottom(logic: BLogicBlock, textures: Column2Values[String]): BJsonGenModelBlock =
		new BJsonGenModelBlock(logic.name) {
			override def apply = modelString("block/slab", textures)
		}
	def defaultTop(logic: BLogicBlock, textures: Column2Values[String]): BJsonGenModelBlock =
		new BJsonGenModelBlock(logic.name+".top") {
			override def apply = modelString("block/slab_top", textures)
		}
	def defaultDouble(logic: BLogicBlock, textures: Column2Values[String]): BJsonGenModelBlock =
		new BJsonGenModelBlock(logic.name+".double") {
			override def apply = BJsonGenModelBlock.simpleModel("block/cube_column", Map(
				("end", textures.end),
				("side", textures.side),
			))
		}
}
