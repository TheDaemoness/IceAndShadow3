package mod.iceandshadow3.lib

import java.util.Random

import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.base.{BLogicWithItem, TLootable}
import mod.iceandshadow3.lib.block.{BlockShape, HarvestMethod}
import mod.iceandshadow3.lib.compat.block.impl.{BVarBlockNew, BinderBlock}
import mod.iceandshadow3.lib.compat.block._
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.{BJsonAssetGen, BJsonAssetGenBlock, BJsonAssetGenBlockstates, BJsonAssetGenItem}
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.world.WWorld

sealed abstract class BLogicBlock(dom: BDomain, name: String, val materia: Materia)
	extends BLogicWithItem(dom, name)
	with BinderBlock.TKey
	with TLootable
{
	BinderBlock.add(this)
	ContentLists.block.add(this)

	def isToolClassEffective(m: HarvestMethod) = materia.isEffective(m)
	def randomlyUpdates: Option[WBlockState => Boolean] = None
	def multipleOpacities = false

	override def stackLimit = 64
	final override def hasItem = isTechnical

	final override def pathPrefix: String = "block"

	/** Whether or not the surfaces of the blocks have any visible holes in them.
		* Controls the rendering layer in conjunction with the materia.
		*/
	override def tier: Int = 1
	def areSurfacesFull = true
	def harvestOverride(block: WBlockRef, fortune: Int): Array[WItemStack] = null
	def harvestXP(what: WBlockView, silktouch: Boolean): Int = 0
	def canStayAt(block: WBlockView, preexisting: Boolean) = true
	//TODO: Separate collision shape from selection shape.
	def shape: BlockShape = BlockShape.FULL_CUBE
	def isDiscrete = false

	def onInside(block: WBlockRef, who: WEntity): Unit = {}
	def onNeighborChanged(us: WBlockRef, them: WBlockRef): WBlockState = us
	def onReplaced(us: WBlockRef, them: WBlockRef, moved: Boolean): Unit = {}
	def onRandomTick(block: WBlockRef, rng: Random): Boolean = true
	def onTick(block: WBlockRef, rng: Random): Unit = {}
	def toPlace(state: WBlockState, context: WUsagePlace): WBlockState = state

	/** Called to provide purely client-side (decorative) effects.
		* Provides a WWorld + WBlockView out of principle, even if we can construct a WBlockRef here.
		*/
	def clientSideTick(client: WWorld, us: WBlockView, rng: Random): Unit = {}
	def variables: Array[BVarBlockNew[_]] = Array.empty

	def toWBlockState: WBlockState = new WBlockState(this)
	override def toWItem = BinderBlock.wrap(this)

	def getBlockModelGen: Option[BJsonAssetGenBlock] = None
	def getBlockstatesGen: Option[BJsonAssetGenBlockstates] = Some(BJsonAssetGen.blockstatesDefault)
	def getItemModelGen: Option[BJsonAssetGenItem[BLogicBlock]] =
		Some(BJsonAssetGen.itemBlockDefault)
}

class LogicBlockSimple(dom: BDomain, name: String, mat: Materia) extends BLogicBlock(dom, name, mat)

abstract class BLogicBlockComplex(dom: BDomain, name: String, mat: Materia) extends BLogicBlock(dom, name, mat) {
	//TODO: Manually generated class stub. For blocks with tile entities.
}
