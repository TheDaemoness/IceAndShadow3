package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.lib.block.ECommonBlockType._
import mod.iceandshadow3.lib.compat.block.BMateriaIce

object MatIcicle extends BMateriaIce {
	override def getName = "icicle"
	override def getBaseHardness = 0f
	override def getBaseBlastResist = 0.25f
	override def getBaseHarvestResist = -1
	override def getShapes = Set(CUBE, FENCE)
	override def getBaseOpacity = 4
	override def getSlipperiness = 0.8f
	override def getBaseLuma = 1
}
