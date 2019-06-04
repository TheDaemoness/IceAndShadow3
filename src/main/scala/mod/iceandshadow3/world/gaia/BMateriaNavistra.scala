package mod.iceandshadow3.world.gaia

import mod.iceandshadow3.basics.util.IMateria
import mod.iceandshadow3.compat.block.BMateriaStone

abstract class BMateriaNavistra extends BMateriaStone {
	override def getBaseBlastResist: Float = IMateria.indestructibleByBlast
	override def getBaseHarvestResist: Int = 5
	override final def isEthereal = false
	override def resistsExousia = true
}
