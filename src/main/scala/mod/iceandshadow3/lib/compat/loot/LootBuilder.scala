package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.compat.item.WItemStack
import net.minecraft.item.ItemStack

import scala.collection.mutable

class LootBuilder[Context <: WLootContext](context: Context) {
	private val lootSources = new mutable.ListBuffer[BLoot[Context]]
	final def addOne(what: BLoot[Context]): this.type = {lootSources.addOne(what); this}
	final def addOne(what: WItemStack): this.type = addOne(BLoot(what))
	private[compat] def results: java.util.List[ItemStack] = {
		import scala.jdk.CollectionConverters._
		lootSources.map(_.apply(this.context)).filter(!_.isEmpty).map(_.asItemStack()).toList.asJava
	}
}
