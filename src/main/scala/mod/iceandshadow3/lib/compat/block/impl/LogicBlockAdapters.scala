package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.common.LogicBlockMateria
import net.minecraft.block.Block

import scala.collection.mutable

private[lib] object LogicBlockAdapters {
	type Adapter = Block with IABlock
	type AdapterFn = BLogicBlock => Adapter

	private class Manager {
		val block: AdapterFn = new ABlock(_)
		val stairs: AdapterFn = new ABlockStairs(_)
		val slab: AdapterFn = new ABlockSlab(_)
		val wall: AdapterFn = new ABlockWall(_)
		private val atypicalAdapters = new mutable.HashMap[BLogicBlock, AdapterFn]()
		def add(what: BLogicBlock, adapter: AdapterFn): Boolean = atypicalAdapters.put(what, adapter).isEmpty
		def apply(what: BLogicBlock): Adapter = atypicalAdapters.getOrElse(what, block)(what)
	}
	/** DO NOT ACCESS DIRECTLY! Access through run to ensure a better exception gets thrown in case of SNAFU. */
	private var obj = new Manager
	private def run[T](fn: Manager => T) = {
		if(obj == null) throw new IllegalStateException("LogicBlockAdapters was disabled")
		fn(obj)
	}

	/** Called from BinderBlock after the freeze happens and none of the contents are needed anymore. */
	private[impl] def disable(): Unit = {
		obj = null
	}

	private[impl] def apply(what: BLogicBlock): Adapter = run(_(what))
	def stairs(what: LogicBlockMateria): LogicBlockMateria = run(m => {m.add(what, m.stairs); what})
	def slab(what: LogicBlockMateria): LogicBlockMateria = run(m => {m.add(what, m.slab); what})
	def wall(what: LogicBlockMateria): LogicBlockMateria = run(m => {m.add(what, m.wall); what})
}
