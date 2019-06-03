package mod.iceandshadow3.basics

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.basics.damage.EDeathPolicy
import mod.iceandshadow3.compat.block.WRegistryBlock
import mod.iceandshadow3.compat.item.{WRarity, WRegistryItem}

import scala.collection.JavaConverters._

/** A collection of lore-related blocks/items/mobs/etc.
	* Every BLogic subtype eventually gets instantiated in and attached to one of these.
	* This affects their IDs as well a number of other (important) properties.
	*/
abstract class BDomain(val name: java.lang.String) {
	Multiverse.addDomain(this)
	private val listLogicBlock = new java.util.LinkedList[BLogicBlock]
	private val listLogicItem = new java.util.LinkedList[BLogicItem]
	
	final protected[basics] def add(l: BLogicBlock): Unit = listLogicBlock.add(l)
	final protected[basics] def add(l: BLogicItem): Unit = listLogicItem.add(l)
	final protected[iceandshadow3] def register(reg: WRegistryBlock): Unit = {
		for(logic <- listLogicBlock.asScala) reg.add(logic)
	}
	final protected[iceandshadow3] def register(reg: WRegistryItem): Unit = {
		for(logic <- listLogicItem.asScala) reg.add(logic)
		for(logic <- listLogicBlock.asScala) reg.add(logic)
	}
	
	protected[iceandshadow3] def initEarly(): Unit = {}
	protected[iceandshadow3] def initLate(): Unit = {}
	def isHostileTo(other: BDomain): Boolean
	def tierToRarity(tier: Int): WRarity
	def tierToDeathPolicy(tier: Int): EDeathPolicy = EDeathPolicy.DEFAULT
}
