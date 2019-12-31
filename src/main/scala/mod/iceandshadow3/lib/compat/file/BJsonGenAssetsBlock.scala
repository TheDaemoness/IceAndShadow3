package mod.iceandshadow3.lib.compat.file

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.misc.CubeValues

abstract class BJsonGenAssetsBlock(logic: BLogicBlock) extends BJsonGenAsset(logic.name) {
	def modelForItemName: Option[String] = models.headOption.map(_.name)
	def models: Seq[BJsonGenModelBlock]
	final override def basePath = "blockstates"
	final def modelNames: Set[String] = {
		val builder = Set.newBuilder[String]
		modelForItemName.foreach(builder.addOne)
		models.foreach(model => builder.addOne(model.name))
		builder.result()
	}
}

object BJsonGenAssetsBlock {
	private def defaultBlockstates(string: String) =
		"{\"variants\":{\"\":{\"model\":\"iceandshadow3:block/"+string+"\"}}}"
	def customSingleModel(logic: BLogicBlock, modelname: String): BJsonGenAssetsBlock = new BJsonGenAssetsBlock(logic) {
		override val apply = defaultBlockstates(modelname)
		override def modelForItemName = Some(modelname)
		override val models = Seq.empty
	}
	def customSingleModel(logic: BLogicBlock): BJsonGenAssetsBlock = customSingleModel(logic, logic.name)
	def basic[Logic <: BLogicBlock](logic: Logic, using: BJsonGenModelBlock) = new BJsonGenAssetsBlock(logic) {
		override val apply = defaultBlockstates(logic.name)
		override val models = Seq(using)
	}
	def cube[Logic <: BLogicBlock](logic: Logic, textures: CubeValues[String]) =
		basic(logic, BJsonGenModelBlock.cube(logic.name, textures))
	def cube[Logic <: BLogicBlock](logic: Logic) = basic(logic, BJsonGenModelBlock.cube(logic))
	def deco[Logic <: BLogicBlock](logic: Logic) = basic(logic, BJsonGenModelBlock.deco(logic))
}
