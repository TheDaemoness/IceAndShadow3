package mod.iceandshadow3.lib.common.model

import com.google.gson.{JsonArray, JsonObject}
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.file.{BJsonGenAssetsBlock, BJsonGenModelBlock}

class JsonGenAssetsBlockWall(
	logic: BLogicBlock,
	inventory: BJsonGenModelBlock, post: BJsonGenModelBlock, side: BJsonGenModelBlock
) extends BJsonGenAssetsBlock(logic){
	def this(logic: BLogicBlock, texture: String) = this(logic,
		JsonGenAssetsBlockWall.defaultInventory(logic.name, texture),
		JsonGenAssetsBlockWall.defaultPost(logic.name, texture),
		JsonGenAssetsBlockWall.defaultSide(logic.name, texture)
	)
	override def modelForItemName = Some(inventory.name)
	override val models = Seq(inventory, post, side)
	override def apply = {
		val parts = Map(
			("up", (false, 0)),
			("north", (true, 0)),
			("east", (true, 90)),
			("south", (true, 180)),
			("west", (true, 270))
		)

		val retval = new JsonObject
		val multipart = new JsonArray
		for((when, (useSide, y)) <- parts) {
			val part = new JsonObject
			; {
				val whenObj = new JsonObject
				whenObj.addProperty(when, "true")
				part.add("when", whenObj)
			}; {
				val applyObj = new JsonObject
				applyObj.addProperty(
					"model",
					s"${IaS3.MODID}:block/"+(if(useSide) side.name else post.name)
				)
				if(y != 0) applyObj.addProperty("y", y)
				if(useSide) applyObj.addProperty("uvlock", true)
				part.add("apply", applyObj)
			}
			multipart.add(part)
		}
		retval.add("multipart", multipart)
		retval.toString
	}
}

object JsonGenAssetsBlockWall {
	private def defaultUsing(name: String, parent: String, texture: String) = new BJsonGenModelBlock(
		name
	) {
		override def apply = BJsonGenModelBlock.simpleModel(parent, Map(
			("wall", texture)
		))
	}
	def defaultInventory(name: String, texture: String) = defaultUsing(name+".item", "block/wall_inventory", texture)
	def defaultPost(name: String, texture: String) = defaultUsing(name+".post", "block/template_wall_post", texture)
	def defaultSide(name: String, texture: String) = defaultUsing(name+".side", "block/template_wall_side", texture)
}
