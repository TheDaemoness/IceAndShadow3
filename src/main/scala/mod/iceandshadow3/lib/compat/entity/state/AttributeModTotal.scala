package mod.iceandshadow3.lib.compat.entity.state

import java.util

import net.minecraft.entity.ai.attributes.AttributeModifier

case class AttributeModTotal(add: Double, addToMult: Double = 0d, mult: Double = 1d) {
	def apply(base: Double) = (base + add) * (1+addToMult) * mult
}
object AttributeModTotal {
	protected[compat] def apply(collection: util.Collection[AttributeModifier]): AttributeModTotal = {
		import scala.jdk.CollectionConverters._
		var add = 0d
		var addToMult = 0d
		var mult = 1d
		for(amod <- collection.asScala) {
			import AttributeModifier.Operation._
			amod.getOperation match {
				case ADDITION => add += amod.getAmount
				case MULTIPLY_BASE => addToMult += amod.getAmount
				case MULTIPLY_TOTAL => mult *= amod.getAmount
			}
		}
		AttributeModTotal(add, addToMult, mult)
	}
}
