package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.block.{HandlerComparator, BlockShape, BlockSubCuboid}
import mod.iceandshadow3.lib.{LogicBlock, LogicTileEntity}
import mod.iceandshadow3.lib.compat.block.{Materia, WBlockRef}
import mod.iceandshadow3.lib.compat.inventory.WContainerSource
import mod.iceandshadow3.lib.compat.entity.{WEntityItem, WEntityPlayer}
import mod.iceandshadow3.lib.compat.file.JsonGenAssetsBlock
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.compat.loot.{LootBuilder, WLootContextBlock}
import mod.iceandshadow3.lib.data.VarSet
import mod.iceandshadow3.multiverse.DomainPolis

class LBBinStorage(name: String, mat: Materia) extends LogicBlock(DomainPolis, name, mat) {
	override val tileEntity = Some(new LogicTileEntity(this.name, VarSet.empty) {
		override val itemCapacity = 27
	})
	override val shape: BlockShape = BlockShape(true, BlockSubCuboid(15))
	override def getGenAssetsBlock = Some(JsonGenAssetsBlock.customSingleModel(this))

	override def container(us: WBlockRef) = WContainerSource.chestlike(us, this.id.translationKey)
	override def onUsed(us: WBlockRef, item: WItemStackOwned[WEntityPlayer]) = {
		us.container().openAs(item.owner)
		true
	}
	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit = {
		super.addDrops(what)
		what.context.inventory.foreach(_.foreach(stack => what.addOne(stack)))
	}

	override val handlerComparator = HandlerComparator(_.inventory().fold(0)(inv => {
		val count = inv.countFilled
		if(count <= 0) 0 else 1+(count*14/inv.size)
	}))

	//TODO: This will degrade performance if the inventory is full. Write something more efficient once support exists.
	override def handlerEntityInside = (us, them) => them match {
		case item: WEntityItem => us.inventory().foreach(inv => if(inv.add(item.item)) item.remove())
		case _ =>
	}
}
