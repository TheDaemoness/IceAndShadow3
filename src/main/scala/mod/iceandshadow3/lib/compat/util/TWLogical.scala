package mod.iceandshadow3.lib.compat.util

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.BStateData
import mod.iceandshadow3.lib.base._
import net.minecraft.nbt.CompoundNBT

trait TWLogical[LogicType <: BLogic] extends TLogicStateProvider[LogicType] {
	this: TLogicProvider[LogicType] =>

	@Nullable
	def registryName: String

	protected def exposeCompoundOrNull(): CompoundNBT

	/** WARNING: This should only be used for read-only access to state data.
		*/
	def exposeStateData(logicpair: LogicPair[LogicType]): LogicType#StateDataType = {
		val sd = logicpair.logic.getDefaultStateData(logicpair.variant)
		if (sd != null) {
			val tree = sd.exposeDataTree()
			//TODO: Consider using WNbtTree here.
			val tags = exposeCompoundOrNull()
			if (tags != null) {
				tree.fromNBT(tags.getCompound(IaS3.MODID))
				sd.fromDataTree(tree)
			}
		}
		sd
	}

	override def getLogicTriad: Option[LogicTriad[LogicType]] =
		Option(getLogicPair).fold[Option[LogicTriad[LogicType]]](None){
			lp => Some(new LogicTriad(lp, exposeStateData(lp)))
		}
	override def toLogicTriad(lp: LogicPair[LogicType]) = new LogicTriad(lp, exposeStateData(lp))

	def saveStateData(state: BStateData): Unit = {
		if (state == null || !state.needsWrite) return
		val tags = exposeCompoundOrNull() //This shouldn't be null in this case, but just in case.
		if(tags != null) tags.put(IaS3.MODID, state.exposeDataTree().toNBT)
		else IaS3.bug(this, "State owner provided state, but cannot save it.")
	}

	def forStateData[T](bsd: BStateData, fn: () => T): T = {
		val retval = fn()
		saveStateData(bsd)
		retval
	}
	def forStateData[T](logictriad: LogicTriad[LogicType], fn: () => T): T = {
		forStateData(logictriad.state, fn)
	}
	def forTriad[T](logicpair: LogicPair[LogicType], fn: LogicTriad[LogicType] => T): T = {
		val triad = toLogicTriad(logicpair)
		val retval = fn(triad)
		saveStateData(triad.state)
		retval
	}
	def forTriad[T](fn: LogicTriad[LogicType] => T): Option[T] = {
		val triad: Option[LogicTriad[LogicType]] = getLogicTriad
		triad.fold[Option[T]](None)(tri => {
			val retval = fn(tri)
			saveStateData(tri.state)
			Some(retval)
		})
	}
}
