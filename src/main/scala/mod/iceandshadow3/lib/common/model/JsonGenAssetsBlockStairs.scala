package mod.iceandshadow3.lib.common.model

import com.google.gson.JsonObject
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.file.{JsonGenAssetsBlock, JsonGenModelBlock}
import mod.iceandshadow3.lib.misc.Column3Values

class JsonGenAssetsBlockStairs(
	logic: BLogicBlock,
	normal: JsonGenModelBlock, outer: JsonGenModelBlock, inner: JsonGenModelBlock,
) extends JsonGenAssetsBlock(logic) {
	def this(logic: BLogicBlock, textures: Column3Values[String]) = this(logic,
		JsonGenAssetsBlockStairs.defaultNormal(logic.name, textures),
		JsonGenAssetsBlockStairs.defaultOuter(logic.name, textures),
		JsonGenAssetsBlockStairs.defaultInner(logic.name, textures)
	)
	override val models = Seq(normal, inner, outer)
	override def apply = {
		val retval = new JsonObject
		val variants = new JsonObject
		retval.add("variants", variants)
		//;_;
		val xMap = Map(("bottom", false), ("top", true))
		val directions = Seq("east", "west", "south", "north")
		val yNormal = Seq(0, 180, 90, 270)
		val yLeft = Seq(270, 90, 0, 180)
		val yRight = Seq(90, 270, 180, 0)
		val coreVariants = Map(
			("straight", (normal, yNormal, yNormal)),
			("outer_right", (outer, yNormal, yRight)),
			("outer_left", (outer, yLeft, yNormal)),
			("inner_right", (inner, yNormal, yRight)),
			("inner_left", (inner, yLeft, yNormal)),
		)
		for(
			(shape, (model, yBottom, yTop)) <- coreVariants;
			(half, x180) <- xMap;
			(facing, y) <- directions.zip(if(x180) yTop else yBottom)
		) {
			val variant = new JsonObject
			variant.addProperty("model", s"${IaS3.MODID}:block/${model.name}")
			if(x180) variant.addProperty("x", 180)
			if(y != 0) variant.addProperty("y", y)
			if(x180 || y != 0) variant.addProperty("uvlock", true)
			variants.add(s"facing=$facing,half=$half,shape=$shape", variant)
		}
		retval.toString
	}
}
object JsonGenAssetsBlockStairs {
	private def defaultUsing(name: String, parent: String, textures: Column3Values[String]) = new JsonGenModelBlock(
		name
	) {
		override def apply = JsonGenModelBlock.simpleModel(parent, Map(
			("bottom", textures.bottom),
			("top", textures.side),
			("side", textures.side)
		))
	}
	def defaultNormal(name: String, textures: Column3Values[String]) =
		defaultUsing(name, "block/stairs", textures)
	def defaultOuter(name: String, textures: Column3Values[String]) =
		defaultUsing(name+".outer", "block/outer_stairs", textures)
	def defaultInner(name: String, textures: Column3Values[String]) =
		defaultUsing(name+".inner", "block/inner_stairs", textures)
}
