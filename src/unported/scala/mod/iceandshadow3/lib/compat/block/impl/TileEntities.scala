package mod.iceandshadow3.lib.compat.block.impl

import com.google.common.collect.HashMultimap
import mod.iceandshadow3.lib.{BLogicBlock, LogicTileEntity}
import net.minecraft.block.Block

object TileEntities {
	private[compat] def collect(iterator: Iterator[BLogicBlock]): Iterator[ATileEntityType] = {
		import scala.jdk.CollectionConverters._

		val multimap = HashMultimap.create[LogicTileEntity, Block]()
		for(logic <- iterator; te <- logic.tileEntity) multimap.put(te, BinderBlock(logic)._1)
		for(key <- multimap.keySet().asScala) BinderTileEntity.add(key, new ATileEntityType(key, multimap.get(key)))
		BinderTileEntity.freeze().iterator
	}

}
