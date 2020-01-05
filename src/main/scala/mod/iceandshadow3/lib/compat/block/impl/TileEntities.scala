package mod.iceandshadow3.lib.compat.block.impl

import java.util

import com.google.common.collect.HashMultimap
import mod.iceandshadow3.lib.{BLogicBlock, LogicTileEntity}
import net.minecraft.block.Block

object TileEntities {
	private[compat] def collect(iterator: Iterator[BLogicBlock]): Iterator[ATileEntityType] = {
		import ATileEntityType.instances
		import scala.jdk.CollectionConverters._

		val multimap = HashMultimap.create[LogicTileEntity, Block]()
		for(logic <- iterator; te <- logic.tileEntity) multimap.put(te, BinderBlock(logic)._1)
		val logicSeq = multimap.keys()
		instances = new util.ArrayList[ATileEntityType](logicSeq.size)
		var index = 0
		val logicSeqIterator = logicSeq.iterator()
		while(logicSeqIterator.hasNext) {
			val logic = logicSeqIterator.next()
			instances.add(new ATileEntityType(logic, index, multimap.get(logic)))
			index += 1
		}
		instances.iterator().asScala
	}

}
