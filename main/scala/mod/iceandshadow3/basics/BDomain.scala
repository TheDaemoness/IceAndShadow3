package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.block.CRegistryBlock
import mod.iceandshadow3.compat.item.CRegistryItem
import mod.iceandshadow3.basics._
import mod.iceandshadow3.compat.item.CRarity
import mod.iceandshadow3.Domains
import java.util.LinkedList
import scala.collection.JavaConverters._

/** A collection of lore-related blocks/items/mobs/etc.
 */
abstract class BDomain(val name: java.lang.String) {
	Domains.addDomain(this)
	private val listLogicBlock = new LinkedList[BLogicBlock]
	private val listLogicItem = new LinkedList[BLogicItem]
	
	final protected[basics] def add(l: BLogicBlock): Unit = listLogicBlock.add(l)
	final protected[basics] def add(l: BLogicItem): Unit = listLogicItem.add(l)
	final protected[iceandshadow3] def register(reg: CRegistryBlock): Unit = {
		for(logic <- listLogicBlock.asScala) reg.add(logic);
	}
	final protected[iceandshadow3] def register(reg: CRegistryItem): Unit = {
		for(logic <- listLogicItem.asScala) reg.add(logic);
		for(logic <- listLogicBlock.asScala) reg.add(logic);
	}
	
	protected[iceandshadow3] def initEarly(): Unit = {}
	protected[iceandshadow3] def initLate(): Unit = {}
	def isHostileTo(other: BDomain): Boolean
	def tierToRarity(tier: Int): CRarity
	def tierToDeathPolicy(tier: Int): EDeathPolicy = EDeathPolicy.DEFAULT
}
