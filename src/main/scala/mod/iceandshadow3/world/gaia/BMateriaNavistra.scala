package mod.iceandshadow3.world.gaia

import mod.iceandshadow3.basics.block.ECommonBlockType._
import mod.iceandshadow3.basics.block.IMateria
import mod.iceandshadow3.compat.block.impl.BMateriaStone

abstract class BMateriaNavistra extends BMateriaStone {
	override def getBaseBlastResist: Float = IMateria.indestructibleByBlast
	override def getBaseHarvestResist: Int = 5
	override final def isNonSolid = false
	override def resistsExousia = true

	override def getSlipperiness = 0.9f
	override def getShapes = Set(CUBE)
}
