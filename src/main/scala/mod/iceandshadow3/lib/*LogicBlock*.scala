package mod.iceandshadow3.lib

import java.util.Random

import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.base.{BLogicWithItem, TLootable}
import mod.iceandshadow3.lib.block.{BlockShape, HarvestMethod}
import mod.iceandshadow3.lib.compat.block.impl.{BVarBlockNew, BinderBlock}
import mod.iceandshadow3.lib.compat.block._
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.{BJsonAssetGen, BJsonAssetGenBlock, BJsonAssetGenBlockstates, BJsonAssetGenItem}
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemType}
import mod.iceandshadow3.lib.compat.world.WWorld

sealed abstract class BLogicBlock(dom: BDomain, name: String, val materia: BMateria)
	extends BLogicWithItem(dom, name)
	with BinderBlock.TKey
	with TLootable
{
	BinderBlock.add(this)
	ContentLists.block.add(this)

	def isToolClassEffective(variant: Int, m: HarvestMethod) = materia.isToolClassEffective(m)
	def randomlyUpdates: Option[WBlockState => Boolean] = None
	def multipleOpacities = false

	override def countVariants = 1
	override def stackLimit(variant: Int) = 64
	final override def hasItem(variant: Int): Boolean = isTechnical

	final override def getPathPrefix: String = "block"

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
	def asWBlockState(variant: Int): WBlockState = new WBlockState(this, variant)

	def variables: Array[BVarBlockNew[_]] = Array.empty

	override def asWItem(variant: Int): WItemType = BinderBlock.wrap(this, variant)

	def getBlockModelGen(variant: Int): Option[BJsonAssetGenBlock] = None
	def getBlockstatesGen(variant: Int): Option[BJsonAssetGenBlockstates] = Some(BJsonAssetGen.blockstatesDefault)
	def getItemModelGen(variant: Int): Option[BJsonAssetGenItem[BLogicBlock]] =
		Some(BJsonAssetGen.itemBlockDefault)
}

class LogicBlockSimple(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat)

abstract class BLogicBlockComplex(dom: BDomain, name: String, mat: BMateria) extends BLogicBlock(dom, name, mat) {
	//TODO: Manually generated class stub. For blocks with tile entities.
}
