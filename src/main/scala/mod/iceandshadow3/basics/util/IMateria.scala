package mod.iceandshadow3.basics.util

object IMateria {
	val indestructibleByMining = -1f
	val indestructibleByBlast = 3600000f
}
trait IMateria {
	def getName: String
	def isToolClassEffective(m: HarvestMethod): Boolean
	def getBaseHardness: Float
	def getBaseBlastResist: Float
	def getBaseHarvestResist: Int
	def getBaseLuma: Int = 0
	def getBaseOpacity: Int
	def isEthereal: Boolean = false
	def getShapes: Set[EBlockShape]
	def resistsExousia: Boolean = false
}
