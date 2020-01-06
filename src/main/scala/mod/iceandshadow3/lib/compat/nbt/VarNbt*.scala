package mod.iceandshadow3.lib.compat.nbt

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.data.BVar
import net.minecraft.nbt._

sealed class VarNbtNumeric[T](
	name: String,
	override val defaultVal: T,
	validId: Int => Boolean,
	fromNumberTag: NumberNBT => T,
	makeTag: T => INBT
) extends BVar[T](name) with TVarNbt[T] {
	override protected[compat] def fromTag(what: INBT): Option[T] = if(validId(what.getId)) {
		val cnvd = try {
			what.asInstanceOf[NumberNBT]
		} catch {
			case classCastException: ClassCastException =>
				IaS3.bug(classCastException, "NBT tag read failure; did NBT tag type ids change?"); return None
		}
		Some(fromNumberTag(cnvd))
	} else None
	override def isDefaultValue(value: T) = value == defaultVal
	override protected def toTag(value: T) = makeTag(value)
}

class VarNbtBool(name: String, default: Boolean)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToNumber, _.getLong > 0, (b: Boolean) => {
	new ByteNBT(if(b) 1 else 0)
})

class VarNbtByte(name: String, default: Byte)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToByte, _.getByte, new ByteNBT(_: Byte))

class VarNbtShort(name: String, default: Short)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToShort, _.getShort, new ShortNBT(_: Short))

class VarNbtInt(name: String, default: Int)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToInt, _.getInt, new IntNBT(_: Int))

class VarNbtLong(name: String, default: Long)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToLong, _.getLong, new LongNBT(_: Long))

class VarNbtFloat(name: String, default: Float)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToFloat, _.getFloat, new FloatNBT(_: Float))

class VarNbtDouble(name: String, default: Double)
extends VarNbtNumeric(name, default, NbtTagUtils.canReadToDouble, _.getDouble, new DoubleNBT(_: Double))

class VarNbtString(name: String, override val defaultVal: String)
extends BVar[String](name) with TVarNbt[String] {
	override def isDefaultValue(value: String) = defaultVal.contentEquals(value)
	override protected def fromTag(what: INBT) = Some(what.getString)
	override protected def toTag(value: String) = new StringNBT(value)
}