package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.compat.item.WItemStack
import net.minecraft.item.ItemStack

import scala.collection.mutable

class LootBuilder[Context <: WLootContext](val context: Context) {
	private val lootSources = new mutable.ListBuffer[Loot[Context]]
	final def clear(): Unit = lootSources.clear()
	final def addOne(what: Loot[Context]): this.type = {lootSources.addOne(what); this}
	final def addOne(what: WItemStack): this.type = addOne(Loot(what))
	private[compat] def results: java.util.List[ItemStack] = {
		import scala.jdk.CollectionConverters._
		lootSources.map(_.apply(this.context)).filter(!_.isEmpty).map(_.expose()).toList.asJava
	}
}
