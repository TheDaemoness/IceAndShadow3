package mod.iceandshadow3.basics

trait IMateria {
	def isToolClassEffective(m: HarvestMethod): Boolean
	def getBaseHardness(): Float
	def getBaseBlastResist(): Float
	def getBaseHarvestResist(): Int
	def isOpaque(): Boolean
	def getBaseLuma(): Int = 0
	def getBaseOpacity(): Int = if (isOpaque()) 0xff else 0
}