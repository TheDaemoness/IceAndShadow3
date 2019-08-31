package mod.iceandshadow3.lib

import java.util.Random

import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.base.TLootable
import mod.iceandshadow3.lib.block.BlockShape
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.compat.block.impl.{BBlockVarNew, BCompatLogicBlock, BinderBlock}
import mod.iceandshadow3.lib.compat.block._
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.world.WWorld

sealed abstract class BLogicBlock(dom: BDomain, name: String, mat: BMateria)
	extends BCompatLogicBlock(dom, name, mat)
	with BinderBlock.TKey
	with TLootable
{
	BinderBlock.add(this)
	ContentLists.block.add(this)

	override def getPathPrefix: String = "block"

	/** Whether or not the surfaces of the blocks have any visible holes in them.
		* Controls the rendering layer in conjunction with the materia.
		*/
	override def getTier(variant: Int): Int = 1
	def areSurfacesFull(variant: Int) = true
	def harvestOverride(variant: Int, block: WBlockRef, fortune: Int): Array[WItemStack] = null
	def harvestXP(variant: Int, what: WBlockView, silktouch: Boolean): Int = 0
	def canStayAt(variant: Int, block: WBlockView, preexisting: Boolean) = true
	//TODO: Separate collision shape from selection shape.
	def shape: BlockShape = BlockShape.FULL_CUBE
	def isDiscrete = false

	//TODO: WBlockRef with TWBlockLogical or some such.

	def onInside(variant: Int, block: WBlockRef, who: WEntity): Unit = {}
	def onNeighborChanged(variant: Int, us: WBlockRef, them: WBlockRef): WBlockState = us
	def onReplaced(variant: Int, us: WBlockRef, them: WBlockRef, moved: Boolean): Unit = {}
	def onRandomTick(variant: Int, block: WBlockRef, rng: Random): Boolean = true
	def onTick(variant: Int, block: WBlockRef, rng: Random): Unit = {}
	def toPlace(state: WBlockState, context: WUsagePlace): WBlockState = state

	/** Called to provide purely client-side (decorative) effects.
		* Provides a WWorld + WBlockView out of principle, even if we can construct a WBlockRef here.
		*/
	def clientSideTick(variant: Int, client: WWorld, us: WBlockView, rng: Random): Unit = {}

	def makeBlockType(variant: Int): TBlockStateSource = new WBlockState(this, variant)
	lazy val _blocktypes = Array.tabulate(countVariants)(makeBlockType)
	def apply(variant: Int) = _blocktypes(variant)

	def variables: Array[BBlockVarNew[_]] = Array.empty
}

abstract class BLogicBlockSimple(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	type StateDataType = BStateData
	override final def getDefaultStateData(variant: Int) = null
}

abstract class BLogicBlockComplex(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	//TODO: Manually generated class stub. For blocks with tile entities.
}
