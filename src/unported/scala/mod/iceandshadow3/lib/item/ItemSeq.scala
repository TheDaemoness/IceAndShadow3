package mod.iceandshadow3.lib.item

import mod.iceandshadow3.lib.compat.item.WItemStack

trait ItemSeq extends scala.collection.mutable.IndexedSeq[WItemStack] {
	def copy: ItemSeq = new ItemSeq {
		private val contents = ItemSeq.this.toArray
		override def update(idx: Int, elem: WItemStack): Unit = contents.update(idx, elem)
		override def apply(i: Int) = contents(i)
		override def length = contents.length
	}
}
