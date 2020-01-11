package mod.iceandshadow3.lib

import java.util.Random
import java.util.function.{BiConsumer, BiFunction, Consumer}

import mod.iceandshadow3.{ContentLists, IaS3}
import mod.iceandshadow3.lib.base.{LogicCommon, TLogicWithItem, TNamed}
import mod.iceandshadow3.lib.block.{BlockShape, HandlerComparator, HarvestMethod}
import mod.iceandshadow3.lib.compat.block.impl.{BinderBlock, VarBlock}
import mod.iceandshadow3.lib.compat.block._
import mod.iceandshadow3.lib.compat.inventory.WContainerSource
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityPlayer}
import mod.iceandshadow3.lib.compat.file.{JsonGen, JsonGenAssetsBlock, JsonGenModelItem}
import mod.iceandshadow3.lib.compat.id.WIdBlock
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned, WItemType}
import mod.iceandshadow3.lib.compat.loot.{Loot, LootBuilder, WLootContextBlock}
import mod.iceandshadow3.lib.data.VarSet
import mod.iceandshadow3.lib.util.E3vl

sealed abstract class BLogicBlock(dom: Domain, baseName: String, val materia: Materia)
	extends LogicCommon(dom, baseName)
	with BinderBlock.TKey
	with TNamed[WIdBlock]
{
	BinderBlock.add(this)
	ContentLists.block.add(this)
	final val id: WIdBlock = new WIdBlock(IaS3.MODID, domain.makeName(baseName))
	final lazy val toWBlockState: WBlockState = new WBlockState(this)
	final override def pathPrefix: String = "block"

	override def itemLogic: Option[LogicBlock]
	def canStayAt(block: WBlockView, preexisting: Boolean) = true
	def harvestXP(what: WBlockView, silktouch: Boolean): Int = 0
	def canDigFast(m: HarvestMethod) = materia.isEffective(m)

	override def tier: Int = 1
	//TODO: Separate collision shape from selection shape.
	def shape: BlockShape = BlockShape.FULL_CUBE
	def isDiscrete = false
	def multipleOpacities = false
	def getGenAssetsBlock: Option[JsonGenAssetsBlock] = Some(JsonGenAssetsBlock.cube(this))

	def randomlyUpdates: Option[WBlockState => Boolean] = None
	def toPlace(state: WBlockState, context: WUsagePlace): WBlockState = state
	def onAdded(us: WBlockRef, them: WBlockState, moving: Boolean): Unit = {}
	def onReplaced(us: WBlockState, them: WBlockRef, moved: Boolean): Unit = {}
	def onNeighborChanged(us: WBlockRef, them: WBlockRef): WBlockState = us
	/** Called on random ticks.
		* @return true if onUpdateTick should also be called by this tick.
		*/
	def onRandomTick(us: WBlockRef, rng: Random): Boolean = true
	/** Called on scheduled update ticks or after onRandomTick. */
	def onUpdateTick(us: WBlockRef, rng: Random): Unit = {}
	//TODO: Expose the BlockRayTraceResult to logics.
	def onUsed(us: WBlockRef, item: WItemStackOwned[WEntityPlayer]): Boolean = false

	/** Whether or not the surfaces of the blocks have any visible holes in them.
		* Controls the rendering layer in conjunction with the materia.
		*/
	def areSurfacesFull = true
	def handlerEntityInside: BiConsumer[WBlockRef, WEntity] = null
	def handlerClientTick: Consumer[WBlockRef] = null
	val handlerComparator: HandlerComparator = HandlerComparator.none
	val variables: VarSet[VarBlock[_]] = VarSet.empty
	val tileEntity: Option[LogicTileEntity] = LogicTileEntity.optionNone
	def container(us: WBlockRef): WContainerSource = WContainerSource.none
	def addDrops(what: LootBuilder[WLootContextBlock]): Unit
}

class LogicBlock(dom: Domain, name: String, mat: Materia)
	extends BLogicBlock(dom, name, mat)
	with TLogicWithItem
{
	override def stackLimit = 64
	override def itemLogic: Option[LogicBlock] = Some(this)
	final override def toWItemType = WItemType.make(this)
	final def toWItemStack = WItemStack.make(this)
	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit = what.addOne(Loot(this).blastDecay)

	def getGenModelItem = {
		def default = Some(JsonGen.modelItemBlockDefault(this))
		//If a block assets gen doesn't exist, assume a custom model with the same name as this logic exists.
		//Else, get the gen it specifies should also be used for the item block. If DNE, require something custom.
		getGenAssetsBlock.fold[Option[JsonGenModelItem]](
			default
		)(
			_.modelForItemName.fold[Option[JsonGenModelItem]](
				None
			)(name =>
				Some(JsonGen.modelItemBlockDefault(this, name))
			)
		)
	}
}

class LogicBlockTechnical(dom: Domain, name: String, mat: Materia) extends BLogicBlock(dom, name, mat) {
	final override def itemLogic = None
	override final def isTechnical = true
	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit = ()
}
