package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.block.ECommonBlockType._
import mod.iceandshadow3.lib.block.IMateria
import mod.iceandshadow3.lib.compat.block.BMateriaStone

abstract class BMateriaStoneLiving extends BMateriaStone

abstract class BMateriaNavistra extends BMateriaStoneLiving {
	override def getBaseBlastResist: Float = IMateria.indestructibleByBlast
	override def getBaseHarvestResist: Int = 5
	override final def isNonSolid = false

	override def getSlipperiness = 0.9f
	override def getShapes = Set(CUBE)
}
