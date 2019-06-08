package mod.iceandshadow3.basics

import mod.iceandshadow3.basics.block.{BlockShape, BlockSides}
import mod.iceandshadow3.compat.block.{BCompatLogicBlock, BMateria, BinderBlock, WRefBlock}
import mod.iceandshadow3.compat.entity.WEntity
import mod.iceandshadow3.compat.item.WRefItem

sealed abstract class BLogicBlock(dom: BDomain, name: String, mat: BMateria)
	extends BCompatLogicBlock(dom, name, mat)
	with BinderBlock.TKey
{
	BinderBlock.add(this)

	/** Whether or not the surfaces of the block have any visible holes in them.
		* Controls the rendering layer in conjunction with the materia.
		*/
	def areSurfacesFull(variant: Int) = true
	def harvestOverride(variant: Int, block: WRefBlock, fortune: Int): Array[WRefItem] = null
	def harvestXP(variant: Int): Int = 0
	def canBeAt(block: WRefBlock, preexisting: Boolean) = true
	//TODO: Separate collision shape from selection shape.
	def shape: BlockShape = BlockShape.FULL_CUBE
	def onInside(block: WRefBlock, who: WEntity): Unit = {}
}

abstract class BLogicBlockSimple(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	type StateDataType = BStateData
	override final def getDefaultStateData(variant: Int) = null
}

abstract class BLogicBlockComplex(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	//TODO: Manually generated class stub. For blocks with tile entities.
}
