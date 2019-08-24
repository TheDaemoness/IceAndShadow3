package mod.iceandshadow3.lib.compat.block.`type`

import java.util.Optional

import mod.iceandshadow3.lib.block.BlockVar
import mod.iceandshadow3.lib.util.OrdSet
import net.minecraft.state.IProperty

import scala.jdk.CollectionConverters._

class AProperty(logic: BlockVar[_]) extends IProperty[Integer] {
	override val getAllowedValues = new OrdSet(logic.size).asJava

	override def getName = logic.name
	override def getValueClass = classOf[Integer]
	override def parseValue(value: String) = {
		val int = logic.parseHalf(value)
		if(int == -1) Optional.empty() else Optional.of(int)
	}
	override def getName(value: Integer) = logic.toString(value)

	override def toString = logic.name+" (AProperty["+logic.size+"])"
}
