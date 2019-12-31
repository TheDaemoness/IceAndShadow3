package mod.iceandshadow3.lib

import java.util.Random

import mod.iceandshadow3.{ContentLists, IaS3}
import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem, TLootable, TNamed}
import mod.iceandshadow3.lib.block.{BlockShape, HarvestMethod}
import mod.iceandshadow3.lib.compat.WIdBlock
import mod.iceandshadow3.lib.compat.block.impl.{BVarBlockNew, BinderBlock}
import mod.iceandshadow3.lib.compat.block._
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.{BJsonGenAsset, BJsonGenAssetsBlock, BJsonGenModelItem}
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemType}
import mod.iceandshadow3.lib.compat.world.WWorld

sealed abstract class BLogicBlock(dom: BDomain, baseName: String, val materia: Materia)
	extends BLogic(dom, baseName)
	with BinderBlock.TKey
	with TLootable
	with TNamed[WIdBlock]
{
	BinderBlock.add(this)
	ContentLists.block.add(this)
	final val id: WIdBlock = new WIdBlock(IaS3.MODID, domain.makeName(baseName))

	def isToolClassEffective(m: HarvestMethod) = materia.isEffective(m)
	def randomlyUpdates: Option[WBlockState => Boolean] = None
	def multipleOpacities = false
	override def itemLogic: Option[LogicBlock]

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
	
	def onInside(us: WBlockRef, who: WEntity): Unit = {}

	def toPlace(state: WBlockState, context: WUsagePlace): WBlockState = state
	def onAdded(us: WBlockRef, them: WBlockState, moving: Boolean): Unit = {}
	def onReplaced(us: WBlockState, them: WBlockRef, moved: Boolean): Unit = {}
	def onNeighborChanged(us: WBlockRef, them: WBlockRef): WBlockState = us
	/** Called on random ticks.
		* @return true if onTick should also be called by this tick.
		*/
	def onRandomTick(us: WBlockRef, rng: Random): Boolean = true
	/** Called on scheduled update ticks or after onRandomTick. */
	def onTick(us: WBlockRef, rng: Random): Unit = {}

	/** Called to provide purely client-side (decorative) effects.
		* Provides a WWorld + WBlockView out of principle, even if we can construct a WBlockRef here.
		*/
	def clientSideTick(client: WWorld, us: WBlockView, rng: Random): Unit = {}
	def variables: Array[BVarBlockNew[_]] = Array.empty

	final lazy val toWBlockState: WBlockState = new WBlockState(this)

	def getGenAssetsBlock: Option[BJsonGenAssetsBlock] = Some(BJsonGenAssetsBlock.cube(this))
}

class LogicBlock(dom: BDomain, name: String, mat: Materia)
	extends BLogicBlock(dom, name, mat)
	with TLogicWithItem
{
	override def stackLimit = 64
	override def itemLogic: Option[LogicBlock] = Some(this)
	final override def toWItemType = WItemType.make(this)
	final def toWItemStack = WItemStack.make(this)

	def getGenModelItem = {
		def default = Some(BJsonGenAsset.modelItemBlockDefault(this))
		//If a block assets gen doesn't exist, assume a custom model with the same name as this logic exists.
		//Else, get the gen it specifies should also be used for the item block. If DNE, require something custom.
		getGenAssetsBlock.fold[Option[BJsonGenModelItem]](
			default
		)(
			_.modelForItemName.fold[Option[BJsonGenModelItem]](
				None
			)(name =>
				Some(BJsonGenAsset.modelItemBlockDefault(this))
			)
		)
	}
}

class LogicBlockTechnical(dom: BDomain, name: String, mat: Materia) extends BLogicBlock(dom, name, mat) {
	final override def itemLogic = None
	override final def isTechnical = true
}
