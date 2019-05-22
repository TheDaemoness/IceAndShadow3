package mod.iceandshadow3.compat

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BStateData
import mod.iceandshadow3.basics.util.LogicPair
import net.minecraft.nbt.NBTTagCompound

abstract class BCRef[LogicType <: BLogic] {
  this: TLogicProvider[LogicType] =>
  protected def exposeNBTOrNull(): NBTTagCompound

  def exposeNbtTree(): CNbtTree = new CNbtTree(exposeNBTOrNull())
  def exposeStateData(logicpair: LogicPair[LogicType]): LogicType#StateDataType = {
    val sd = logicpair.logic.getDefaultStateData(logicpair.variant)
    if (sd != null) {
      val tree = sd.exposeDataTree()
      //TODO: Consider using CNbtTree here.
      val tags = exposeNBTOrNull()
      if (tags != null) {
        tree.fromNBT(tags.getCompound(IaS3.MODID))
        sd.fromDataTree(tree)
      }
    }
    sd
  }
  def exposeStateData(): LogicType#StateDataType = {
    val logicpair = getLogicPair
    if(logicpair == null) null.asInstanceOf[LogicType#StateDataType] else exposeStateData(getLogicPair)
  }

  def saveStateData(state: BStateData): Unit = {
    if (state == null || !state.needsWrite) return
    val tags = exposeNBTOrNull() //This shouldn't be null in this case, but just in case.
    if(tags != null) tags.setTag(IaS3.MODID, state.exposeDataTree().toNBT)
    else IaS3.bug(new IllegalArgumentException, "Attempted to save state to an instance that can't save it, and therefore shouldn't have returned any in the first place.")
  }

  def forStateData[T](logicpair: LogicPair[LogicType], fn: BStateData => T): T = {
    val statedata = exposeStateData(logicpair)
    val retval = fn(statedata)
    saveStateData(statedata)
    retval
  }
  def forStateData[T](fn: BStateData => T): Option[T] = {
    val pair = getLogicPair
    if(pair != null) Some(forStateData[T](pair, fn)) else None
  }
}
