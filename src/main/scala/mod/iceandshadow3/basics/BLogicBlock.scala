package mod.iceandshadow3.basics

import java.util.Random

import mod.iceandshadow3.basics.block.BlockShape
import mod.iceandshadow3.compat.block.impl.{BCompatLogicBlock, BMateria, BinderBlock}
import mod.iceandshadow3.compat.block.{WBlockRef, WBlockView}
import mod.iceandshadow3.compat.entity.WEntity
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.compat.world.WWorld

sealed abstract class BLogicBlock(dom: BDomain, name: String, mat: BMateria)
	extends BCompatLogicBlock(dom, name, mat)
	with BinderBlock.TKey
{
	BinderBlock.add(this)

	override def resistsExousia(variant: Int) = mat.resistsExousia

	/** Whether or not the surfaces of the blocks have any visible holes in them.
		* Controls the rendering layer in conjunction with the materia.
		*/
	override def getTier(variant: Int): Int = 1
	def areSurfacesFull(variant: Int) = true
	def harvestOverride(variant: Int, block: WBlockRef, fortune: Int): Array[WItemStack] = null
	def harvestXP(variant: Int, what: WBlockView, silktouch: Boolean): Int = 0
	def canBeAt(variant: Int, block: WBlockView, preexisting: Boolean) = true
	//TODO: Separate collision shape from selection shape.
	def shape: BlockShape = BlockShape.FULL_CUBE
	def isDiscrete = false

	//TODO: WBlockRef with TWBlockLogical or some such.

	def onInside(variant: Int, block: WBlockRef, who: WEntity): Unit = {}
	def onNeighborChanged(variant: Int, us: WBlockRef, them: WBlockRef): Unit = {}

	/** Called to provide purely client-side (decorative) effects.
		* Provides a WWorld + WBlockView out of principle, even if we can construct a WBlockRef here.
		*/
	def clientSideTick(variant: Int, client: WWorld, us: WBlockView, rng: Random) = {}
}

abstract class BLogicBlockSimple(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	type StateDataType = BStateData
	override final def getDefaultStateData(variant: Int) = null
}

abstract class BLogicBlockComplex(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	//TODO: Manually generated class stub. For blocks with tile entities.
}
