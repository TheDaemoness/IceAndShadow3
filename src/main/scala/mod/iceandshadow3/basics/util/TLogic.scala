package mod.iceandshadow3.basics.util

import mod.iceandshadow3.basics.{BDomain, BStateData}

trait TLogic {
	type StateDataType <: BStateData

	def countVariants: Int = 1
	def isTechnical(variant: Int): Boolean = false
	def getTier(variant: Int): Int = 1
	def getDomain: BDomain
	def getDefaultStateData(variant: Int): StateDataType

	protected def getBaseName: String
	protected def getVariantName(variant: Int): String = null

	final def getName: String = getDomain.name+'_'+getBaseName
	final def getName(variant: Int): String = {
		val varname = getVariantName(variant)
		if(varname == null) getName else getName+'_'+varname
	}
	def resistsExousia(variant: Int): Boolean
}
