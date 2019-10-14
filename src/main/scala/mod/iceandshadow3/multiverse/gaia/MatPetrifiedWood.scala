package mod.iceandshadow3.multiverse.gaia

object MatPetrifiedWood extends BMateriaStoneLiving with TMateriaGrowable {
	def getName = "petrified_wood"
	def getBaseHardness: Float = 5f
	def getBaseHarvestResist: Int = 2

	override def getBaseBlastResist = getBaseHardness*3f
}
