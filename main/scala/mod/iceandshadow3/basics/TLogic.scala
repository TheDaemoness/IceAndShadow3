package mod.iceandshadow3.basics

import mod.iceandshadow3.BDomain
import mod.iceandshadow3.compat.BLogic

trait TLogic {
	def countVariants(): Int = 1
	def isTechnical(): Boolean = false
	def getTier(variant: Int): Int = 1
	def getDomain(): BDomain
	protected def getBaseName(): String
	protected def getVariantName(variant: Int): String = null
	
	final def getName(): String = getDomain().name+'_'+getBaseName()
	final def getName(variant: Int): String = {
		val varname = getVariantName(variant)
		if(varname == null) getName() else getName()+'_'+varname
	}
}

trait ILogicProvider[LogicType <: BLogic] {
	def getLogic(): LogicType
}
