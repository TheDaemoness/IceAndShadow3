package mod.iceandshadow3.basics

trait IMateria {
	def getName: String
	def isToolClassEffective(m: HarvestMethod): Boolean
	def getBaseHardness: Float
	def getBaseBlastResist: Float
	def getBaseHarvestResist: Int
	def getBaseLuma: Int = 0
	def getBaseOpacity: Int
	def isEthereal: Boolean = false
}