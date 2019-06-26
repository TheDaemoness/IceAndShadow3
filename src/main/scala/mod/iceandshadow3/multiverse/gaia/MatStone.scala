package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.compat.block.impl.BMateriaStone

object MatStone extends BMateriaStone {
	def getName = "livingstone"
	def getBaseBlastResist: Float = 15f
	def getBaseHardness: Float = 2f
	def getBaseHarvestResist: Int = 1
}