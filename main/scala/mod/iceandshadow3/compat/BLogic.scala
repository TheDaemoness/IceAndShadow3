package mod.iceandshadow3.compat

import mod.iceandshadow3.BDomain
import mod.iceandshadow3.basics.TLogic

abstract class BLogic(protected val domain: BDomain, protected val name: String) extends TLogic {
	private[compat] var secrets: SecretsLogic[_ <: TLogic, _] = null
	private[compat] def getSecrets[McType <: Object] = secrets.asInstanceOf[SecretsLogic[this.type,McType]];
	
	override def getBaseName(): String = name
	override def getDomain(): BDomain = domain
}