package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.common.LogicBlockMateria
import net.minecraft.block.Block

import scala.collection.mutable

private[lib] object LogicBlockAdapters {
	type Adapter = Block with IABlock
	type AdapterGen = () => Adapter

	private class Manager {
		def defaultWrap(what: BLogicBlock): AdapterGen = () => new ABlock(what)
		def stairs(what: LogicBlockMateria): AdapterGen = () => new ABlockStairs(what)
		def slab(what: LogicBlockMateria): AdapterGen = () => new ABlockSlab(what)
		def wall(what: LogicBlockMateria): AdapterGen = () => new ABlockWall(what)
		private val atypicalAdapters = new mutable.HashMap[BLogicBlock, AdapterGen]()
		def add(what: BLogicBlock, adapter: AdapterGen): Boolean = atypicalAdapters.put(what, adapter).isEmpty
		def apply(key: BLogicBlock): Adapter = atypicalAdapters.getOrElse(key, defaultWrap(key))()
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
	def stairs(what: LogicBlockMateria): LogicBlockMateria = run(m => {m.add(what, m.stairs(what)); what})
	def slab(what: LogicBlockMateria): LogicBlockMateria = run(m => {m.add(what, m.slab(what)); what})
	def wall(what: LogicBlockMateria): LogicBlockMateria = run(m => {m.add(what, m.wall(what)); what})
}
