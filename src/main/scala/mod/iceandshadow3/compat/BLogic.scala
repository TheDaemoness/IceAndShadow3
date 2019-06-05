package mod.iceandshadow3.compat

import mod.iceandshadow3.basics.BDomain
import mod.iceandshadow3.basics.util.TLogic
import mod.iceandshadow3.forge.fish.IEventFish

import scala.reflect.{ClassTag, classTag}

abstract class BLogic(protected val domain: BDomain, protected val name: String) extends TLogic {
	override def getBaseName: String = name
	override def getDomain: BDomain = domain

	def getEventFish[T <: IEventFish: ClassTag](variant: Int): Option[T] = {
		val fishtype = classTag[T].runtimeClass
		if (fishtype.isAssignableFrom(this.getClass)) Some(fishtype.cast(this).asInstanceOf[T]) else None
	}
}