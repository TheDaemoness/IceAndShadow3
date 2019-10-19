package mod.iceandshadow3.lib.block

import scala.reflect.{ClassTag, classTag}

//TODO: Rework materia in general.

object IMateria {
	val indestructibleByMining = -1f
	val indestructibleByBlast = 3600000f
}
trait IMateria {
	def getName: String
	def isToolClassEffective(m: HarvestMethod): Boolean
	def getBaseHardness: Float
	def getBaseBlastResist: Float = getBaseHardness*5f
	def getBaseHarvestResist: Int
	def getBaseLuma: Int = 0
	def getBaseOpacity: Int
	def getSlipperiness: Float = 0.6f
	def isNonSolid: Boolean = false
	def getShapes: Set[ECommonBlockType]
	def isTransparent = false
	def isTypeOf[Type <: IMateria: ClassTag]: Boolean = classTag[Type].runtimeClass.isInstance(this)
}
