package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.common.LogicBlockMateria
import net.minecraft.block.Block

import scala.collection.mutable

private[lib] object LogicBlockAdapters {
	type Adapter = Block with IABlock
	type AdapterFn = BLogicBlock => Adapter

	private var _default: AdapterFn = new ABlock(_)
	private var _stairs: AdapterFn = new ABlockStairs(_)
	private var _slab: AdapterFn = new ABlockSlab(_)
	private var _wall: AdapterFn = new ABlockWall(_)

	private var atypicalAdapters = new mutable.HashMap[BLogicBlock, AdapterFn]()
	/** Called from BinderBlock after the freeze happens and none of the contents are needed anymore. */
	private[impl] def disable(): Unit = {
		atypicalAdapters = null
		_default = null
		_stairs = null
		_slab = null
		_wall = null
	}
	private[impl] def apply(what: BLogicBlock): Adapter = {
		if(atypicalAdapters == null) throw new IllegalStateException("LogicBlockWrappers used after being disabled")
		atypicalAdapters.getOrElse(what, _default)(what)
	}
	def stairs(what: LogicBlockMateria): LogicBlockMateria = {atypicalAdapters.put(what, _stairs); what}
	def slab(what: LogicBlockMateria): LogicBlockMateria = {atypicalAdapters.put(what, _slab); what}
	def wall(what: LogicBlockMateria): LogicBlockMateria = {atypicalAdapters.put(what, _wall); what}
}
