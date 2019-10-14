package mod.iceandshadow3.multiverse.gaia

object MatStone extends BMateriaStoneLiving with TMateriaGrowable {
	def getName = "livingstone"
	def getBaseHardness: Float = 3f
	def getBaseHarvestResist: Int = 1
}